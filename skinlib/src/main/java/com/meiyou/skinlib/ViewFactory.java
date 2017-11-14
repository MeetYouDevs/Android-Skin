/*

package com.meiyou.skinlib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meiyou.skinlib.attr.MutableAttr;
import com.meiyou.skinlib.attr.MutableAttrFactory;
import com.meiyou.skinlib.util.LogUtils;
import com.meiyou.skinlib.util.ReflectUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

*/
/**
 * warn :LayoutInflater.from(this.context); 并不总是同一个对象
 *//*

public class ViewFactory implements LayoutInflater.Factory2, RuntimeGenView {
    private static final String sTAG = "ViewFactory";
    private WeakHashMap<View, List<MutableAttr>> holderMap = new WeakHashMap<>();
    static private Field contextField;
    static private Class fragmentManagerClazz;
    static private Method createViewMethod;
    static private Field mFragmentsField;
    private WeakHashMap<Context, WeakReference<LayoutInflater>> mInflaterWeakHashMap = new WeakHashMap<>();
    //WeakReference<LayoutInflater> layoutInflater;// last layoutInflater
    */
/**
     * 不同的Context 尤其是Activity 具有更强的能力,比如构造fragment
     *//*

    WeakReference<Context> refContext;  //极端的场景下,layoutInflater 总是为null, 可以用来构造出layoutInflater
    static ViewFactory sInstance;

    private ViewFactory(Context context) {
        WeakReference<LayoutInflater> layoutInflater = new WeakReference<>(initFactory(context));
        this.refContext = new WeakReference<>(context);
        this.mInflaterWeakHashMap.put(context, layoutInflater);
    }

    public static ViewFactory from(Context context) {
        if (sInstance == null) {
            synchronized (ViewFactory.class) {
                if (sInstance == null) {
                    sInstance = new ViewFactory(context);
                }
            }
        }
        sInstance.refContext = new WeakReference<>(context);
        return sInstance;
    }


    private LayoutInflater initFactory(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context).cloneInContext(context);
        try {
            if (contextField == null) {
                contextField = LayoutInflater.class.getDeclaredField("mConstructorArgs");
                contextField.setAccessible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // can only set once
        LayoutInflaterCompat.setFactory2(layoutInflater, this);

        return layoutInflater;
    }

    */
/**
     * 先获取最后一个 LayoutInflater
     * 否则从 map 获取
     * 否则从 context创建
     *
     * @return
     *//*

    public LayoutInflater getLayoutInflater() {
        LayoutInflater layoutInflater = null;
        if (this.refContext.get() != null) {
            layoutInflater = getLayoutInflater(refContext.get());
        } else {
            for (Context context : mInflaterWeakHashMap.keySet()) {
                WeakReference<LayoutInflater> ref = mInflaterWeakHashMap.get(context);
                if (ref.get() != null) {
                    layoutInflater = ref.get();
                    break;
                }
            }
        }
        return layoutInflater;
    }

    public LayoutInflater getLayoutInflater(Context context) {
        LayoutInflater inflater = null;
        WeakReference<LayoutInflater> weakReference = mInflaterWeakHashMap.get(context);
        if (weakReference != null) {
            inflater = weakReference.get();
        }
        if (inflater == null) {
            inflater = initFactory(context);
        }
        mInflaterWeakHashMap.put(context, new WeakReference<>(inflater));
        return inflater;
    }
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attributeSet) {
        View view = createView(name, context, attributeSet);
        List<MutableAttr> viewAttrs = saveAttrs(context, view, attributeSet);
        if (viewAttrs != null
                && !viewAttrs.isEmpty()
                && AndroidSkin.getInstance().isSkinApply()) {
            for (MutableAttr attr : viewAttrs) {
                try {
                    attr.apply(view);
                } catch (Exception e) {
                    //LogUtils.w(sTAG, e.getLocalizedMessage());
                }
            }
        }
        return view;
    }
    @Override
    public View onCreateView(String name, Context context, AttributeSet attributeSet) {
        return null;
    }

    private List<MutableAttr> saveAttrs(Context context, View view, AttributeSet attrs) {
        List<MutableAttr> viewAttrs = new ArrayList<>();
        if (view == null) {
            return viewAttrs;
        }

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (!MutableAttr.support(attrName)) {
                continue;
            }

            if (attrValue.startsWith("@")) {
                if (attrValue.startsWith("@style/") || attrValue.startsWith("@android:style/")) {
                    viewAttrs = ReflectUtil.processStyle(context,view, attrs, attrValue);
                } else {
                    try {
                        int id = Integer.parseInt(attrValue.substring(1));
                        if (id == 0 ) {
                            if (!viewAttrs.isEmpty()) {
                                holderMap.put(view, viewAttrs);
                            }
                            return viewAttrs;
                        }
                        String entryName = context.getResources().getResourceEntryName(id);
                        String typeName = context.getResources().getResourceTypeName(id);
                        MutableAttr mutableAttr = MutableAttrFactory.create(attrName, id, entryName, typeName);
                        if (mutableAttr != null) {
                            viewAttrs.add(mutableAttr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        if (!viewAttrs.isEmpty()) {
            holderMap.put(view, viewAttrs);
        }
        return viewAttrs;
    }


    @Nullable
    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        LayoutInflater layoutInflater = getLayoutInflater(context);
        if (layoutInflater == null) {
            LogUtils.d(sTAG, "layoutInflater is null! cannot createview!");
            return null;
        }

        String prefix = null;
        view = processFragment(name, context, attrs);
        if (view != null) return view;

        try {
            // need set again because of  set null by  layoutInflater in inflte method
            Object[] params = new Object[]{context, null};
            contextField.set(layoutInflater, params);
            //注意 有些view 从  1.0 到现在 可能包名被改变了
            if (-1 == name.indexOf('.')) {
                try {
                    if ("View".equals(name)) {
                        view = layoutInflater.createView(name, "android.view.", attrs);
                    }
                } catch (Exception e) {
                    LogUtils.d(sTAG, "create view failed " + name);
                }
                try {
                    if (view == null && !"WebView".equalsIgnoreCase(name)) {
                        view = layoutInflater.createView(name, "android.widget.", attrs);
                    }
                } catch (Exception e) {
                    LogUtils.d(sTAG, "create view failed " + name);
                }
                try {
                    if (view == null) {
                        view = layoutInflater.createView(name, "android.webkit.", attrs);
                    }
                } catch (Exception e) {
                    LogUtils.d(sTAG, "create view failed " + name);
                }

            } else {
                view = layoutInflater.createView(name, null, attrs);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                // release the context
                Object[] params = new Object[]{null, null};
                contextField.set(layoutInflater, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    */
/**
     * invoke method
     * final View v = mFragments.onCreateView(null, name, context, attrs);
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     *//*

    private View processFragment(String name, Context context, AttributeSet attrs) {
        if ("fragment".equals(name)) { //fragment 对象本身的创建走默认方式
            LogUtils.d(sTAG, "create fragment tag!");
            FragmentActivity activity = (FragmentActivity) context;
            View view = null;
            if (activity != null) {
                try {
                    if (mFragmentsField == null) {
                        //得到FragmentManagetImpl对象
                        */
/*fragmentManagerClazz = Class.forName("android.support.v4.app.FragmentActivity");
                        Method getSupportManagerImplMethod = fragmentManagerClazz.getDeclaredMethod("getSupportFragmentManager");
                        getSupportManagerImplMethod.setAccessible(true);
                        Class getSupportManagerImplClass = (Class)getSupportManagerImplMethod.invoke(fragmentManagerClazz);

                        //调用FragmentMnagerImpl的onCreateView方法
                        Class<?>[] params = new Class<?>[]{View.class, String.class, Context.class, AttributeSet.class};
                        createViewMethod = getSupportManagerImplClass.getDeclaredMethod("onCreateView", params);
                        createViewMethod.setAccessible(true);*//*



                        mFragmentsField = FragmentActivity.class.getDeclaredField("mFragments");
                        mFragmentsField.setAccessible(true);
                        fragmentManagerClazz = Class.forName("android.support.v4.app.FragmentManager$FragmentManagerImpl");
                        Class<?>[] params = new Class<?>[]{View.class, String.class, Context.class, AttributeSet.class};
                        createViewMethod = fragmentManagerClazz.getDeclaredMethod("onCreateView", params);
                        createViewMethod.setAccessible(true);

                    }
                    if(createViewMethod!=null  && mFragmentsField!=null)
                        view = (View) createViewMethod.invoke(mFragmentsField.get(activity), name, context, attrs);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    mFragmentsField=null;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    mFragmentsField=null;
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    mFragmentsField=null;
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    mFragmentsField=null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    mFragmentsField=null;
                }
            }
            return view;
        }
        return null;
    }

    public boolean apply() {
        Iterator<WeakHashMap.Entry<View, List<MutableAttr>>>
                iterator = holderMap.entrySet().iterator();
        while (iterator.hasNext()) {
            WeakHashMap.Entry<View, List<MutableAttr>> entry = iterator.next();
            View view = entry.getKey();
            List<MutableAttr> list = entry.getValue();
            if (view != null && list != null && !list.isEmpty()) {
                for (MutableAttr attr : list) {
                    try {
                        attr.apply(view);
                    } catch (Exception e) {
                        LogUtils.d(sTAG, e.getLocalizedMessage());
                    }
                }
            }
        }
        return true;

    }

    public boolean apply(View view) {
        if (view == null) {
            return true;
        }
        List<MutableAttr> list = holderMap.get(view);
        if (list != null && !list.isEmpty()) {
            for (MutableAttr attr : list) {
                try {
                    attr.apply(view);
                } catch (Exception e) {
                    LogUtils.d(sTAG, e.getLocalizedMessage());
                }
            }
        }
        return true;
    }

    public boolean apply(ViewGroup viewGroup) {
        apply((View) viewGroup);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                apply((ViewGroup) view);
            } else {
                apply(view);
            }
        }
        return true;
    }


    @Override
    public void addRuntimeView(View view, List<MutableAttr> mutableAttrList) {
        if (view == null || mutableAttrList == null || mutableAttrList.isEmpty()) {
            return;
        }
        if (checkAllNull(mutableAttrList)) return;
        holderMap.put(view, mutableAttrList);
    }

    private boolean checkAllNull(List<MutableAttr> mutableAttrList) {
        boolean empty = true;
        for (MutableAttr mutableAttr : mutableAttrList) {
            if (mutableAttr != null) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    public void excludeView(View view) {
        holderMap.remove(view);
    }

    public void excludeViewAttrs(View view, String... attrs) {
        if (attrs == null || attrs.length <= 0) {
            return;
        }
        Set<String> attrSet = new HashSet<>(Arrays.asList(attrs));
        List<MutableAttr> mutableAttrList = holderMap.get(view);
        if (mutableAttrList != null && !mutableAttrList.isEmpty()) {
            for (int i = mutableAttrList.size() - 1; i >= 0; i--) {
                if (attrSet.contains(mutableAttrList.get(i).attrName)) {
                    mutableAttrList.remove(i);
                }
            }
        }
    }

    */
/**
     * @param attrName       color , background
     * @param attrValueRefId a int value ,  such as  R.color.red
     *//*

    @Override
    public MutableAttr createMutableAttr(String attrName, int attrValueRefId) {
        if (!AndroidSkin.getInstance().isInited()) {
            LogUtils.d(sTAG, "SkinManger is not ready ! cannot createMutableAttr ");
            return null;
        }
        String attrValueRefName = AndroidSkin.getInstance().getAndroidSkinManager().getAndroidSkinResources().getResourceEntryName(attrValueRefId);
        String typeName = AndroidSkin.getInstance().getAndroidSkinManager().getAndroidSkinResources().getResourceTypeName(attrValueRefId);
        return MutableAttrFactory.create(attrName, attrValueRefId, attrValueRefName, typeName);
    }



}
*/
