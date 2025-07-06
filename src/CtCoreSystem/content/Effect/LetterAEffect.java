package CtCoreSystem.content.Effect;


import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.ui.Fonts;

public class LetterAEffect extends Effect{
    // 字体对象(使用Mindustry默认字体)
    private static Font font = Fonts.outline;
    // 字母大小
    private static float fontSize = 5f;
    // 字母颜色
    private static Color letterColor = Color.white;

    public LetterAEffect(){
        // 设置效果持续时间为180帧(3秒)
        lifetime = 180f;
    }

    @Override
    public void render(EffectContainer container){
        // 获取当前进度(0到1)
        float progress = container.fin();

        // 计算透明度(结束时淡出)
        float alpha = Mathf.lerp(1f, 0f, progress * 1.2f);

        // 设置颜色和字体
        Draw.color(letterColor);
        font.setColor(letterColor);
        font.getData().setScale(fontSize);

        // 绘制字母"A"
        font.draw(
                "A",                           // 文本内容
                container.x,                   // X坐标
                container.y + fontSize * 10f,  // Y坐标(向上偏移)
                1,                             // 水平对齐(0=居中)
                Align.center,                  // 垂直对齐
                false                         // 是否换行
        );

        // 重置绘制状态
        Draw.reset();
    }

    // 静态方法方便调用
    public static void show(){
        new LetterAEffect();
    }
}