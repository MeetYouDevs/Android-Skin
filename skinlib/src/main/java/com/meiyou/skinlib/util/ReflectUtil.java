package com.meiyou.skinlib.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.meiyou.skinlib.attr.MutableAttr;
import com.meiyou.skinlib.attr.MutableAttrFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 主要用于反射 style
 */
public class ReflectUtil {

    static String rStyleClazzName = "com.android.internal.R$styleable";
    static Class clazz;

    // static Field header;
    // static Field footer;
    // 支持的原生控件
    final static String[] supportAndroidWidget = new String[] {"View", "TextView", "ImageView"};
    // 支持的原生属性，必须按照生成的attrId顺序排列
    final static String[] supportAndroidAttrs = new String[] {"TextView_textColor", "TextView_textColorHint",
        "View_background", "ImageView_src"};
    /**
     * 支持属性对应可识别的attrName，需要用枚举里的realName，并且顺序与supportAndroidAttrs一致
     */
    final static String[] supportAndroidAttrsRealName = new String[] {MutableAttr.TYPE.TEXT_COLOR.getRealName(),
        MutableAttr.TYPE.HINT_TEXT_COLOR.getRealName(), MutableAttr.TYPE.BACKGROUND.getRealName(),
        MutableAttr.TYPE.SRC.getRealName()};
    final static int[] supportAndroidAttrIds = new int[supportAndroidAttrs.length];

    public static List<MutableAttr> processStyle(Context context, View view, AttributeSet attrs, String attrValue) {
        List<MutableAttr> viewAttrs = new ArrayList<>();
        TypedArray a = null;
        try {

            if (clazz == null) {
                clazz = Class.forName(rStyleClazzName);
                Field[] widgetField = new Field[supportAndroidWidget.length];
                for (int i = 0; i < supportAndroidWidget.length; i++) {
                    widgetField[i] = clazz.getDeclaredField(supportAndroidWidget[i]);
                }
                for (int i = 0; i < supportAndroidAttrs.length; i++) {
                    Field attrField = clazz.getDeclaredField(supportAndroidAttrs[i]);
                    int index = attrField.getInt(null);
                    String widgetStr = supportAndroidAttrs[i].split("_")[0];
                    for (int j = 0; j < supportAndroidWidget.length; j++) {
                        if (widgetStr.equals(supportAndroidWidget[j])) {
                            int[] widgetAllAttrs = (int[]) widgetField[j].get(null);
                            int attrId = widgetAllAttrs[index];
                            supportAndroidAttrIds[i] = attrId;
                            break;
                        }
                    }
                }
            }
            // view的stytle命中
            // a = context.obtainStyledAttributes(attrs, attributes, 0, 0);
            // fillData(context, a, viewAttrs);
            // // 从textview再取一遍,否则style里的textcolor都会换肤失败;无法命中
            // a = context.obtainStyledAttributes(attrs, attributesTv, 0, 0);
            // fillData(context, a, viewAttrs);
            // // 从ImageView再取一遍
            // a = context.obtainStyledAttributes(attrs, attributesIv, 0, 0);
            // fillData(context, a, viewAttrs);
            int textColorResId = Resources.getSystem().getIdentifier("textColor", "attr", "android");
            int textColorHintResId = Resources.getSystem().getIdentifier("textColorHint", "attr", "android");
            int backgroudResId = Resources.getSystem().getIdentifier("background", "attr", "android");
            int srcResId = Resources.getSystem().getIdentifier("src", "attr", "android");
            a = context.obtainStyledAttributes(attrs, supportAndroidAttrIds, 0, 0);
            fillData(context, a, viewAttrs);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (a != null)
                a.recycle();
        }
        return viewAttrs;
    }

    private static void fillData(Context context, TypedArray a, List<MutableAttr> viewAttrs) {
        String attrName;
        int N = a.getIndexCount();
        int resId;
        for (int j = 0; j < N; j++) {
            int index = a.getIndex(j);
            attrName = supportAndroidAttrsRealName[index];
            resId = a.getResourceId(index, 0);
            if (resId == 0) {
                continue;
            }
            String entryName = context.getResources().getResourceEntryName(resId);
            // 防止被Theme.Light的颜色覆盖,导致换肤失败
            if (entryName != null
                && (entryName.equals("hint_foreground_light")
                    || entryName.equals("bright_foreground_disabled_material_light")
                    || entryName.contains("_foreground_") || entryName.contains("_material_")))
                // hint_foreground_light hint_foreground_material_light ,bright_foreground_disabled_material_light
                continue;
            String typeName = context.getResources().getResourceTypeName(resId);
//            MutableAttr mutableAttr = MutableAttrFactory.create(attrName, resId, entryName, typeName);
//            if (mutableAttr != null) {
//                viewAttrs.add(mutableAttr);
//            }
        }
    }
    /* @Deprecated
     private void collectView(Set<View> set, ViewGroup rootView, Set<View> excludeViewSet) {
         if (excludeViewSet == null) excludeViewSet = new HashSet<>();
         if (excludeViewSet.contains(rootView)) {
             return;
         }
         set.add(rootView);
         for (int i = 0; i < rootView.getChildCount(); ++i) {
             View view = rootView.getChildAt(i);
             set.add(view);
             if (view instanceof ListView) {
                 addForListHeaderAndFooter(set, (ListView) view, excludeViewSet);//只添加header/footer  ,children 等下收集
             }
             if (view instanceof ViewGroup) {
                 collectView(set, (ViewGroup) view, excludeViewSet);
             }
         }
     }
     @Deprecated
     private void addForListHeaderAndFooter(Set<View> set, ListView listView, Set<View> excludeViewSet) {

         try {
             if (header == null || footer == null) {
                 header = ListView.class.getDeclaredField("mHeaderViewInfos");
                 footer = ListView.class.getDeclaredField("mFooterViewInfos");
                 header.setAccessible(true);
                 footer.setAccessible(true);
             }

             ArrayList<ListView.FixedViewInfo> headerInfos = (ArrayList<ListView.FixedViewInfo>) header.get(listView);
             ArrayList<ListView.FixedViewInfo> footerInfos = (ArrayList<ListView.FixedViewInfo>) footer.get(listView);
             ArrayList<ListView.FixedViewInfo> allInfos = new ArrayList<>();
             allInfos.addAll(headerInfos);
             allInfos.addAll(footerInfos);
             for (ListView.FixedViewInfo info : allInfos) {
                 set.add(info.view);
                 if (info.view instanceof ViewGroup) {
                     collectView(set, (ViewGroup) info.view, excludeViewSet);
                 }
             }

         } catch (NoSuchFieldException e) {
             e.printStackTrace();
         } catch (IllegalAccessException e) {
             e.printStackTrace();
         }
     }*/
}
