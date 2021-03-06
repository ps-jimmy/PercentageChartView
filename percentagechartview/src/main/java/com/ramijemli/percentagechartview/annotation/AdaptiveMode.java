package com.ramijemli.percentagechartview.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

import static com.ramijemli.percentagechartview.renderer.BaseModeRenderer.DARKER_MODE;
import static com.ramijemli.percentagechartview.renderer.BaseModeRenderer.LIGHTER_MODE;

@Retention(RetentionPolicy.SOURCE)
@IntDef({DARKER_MODE, LIGHTER_MODE})
public @interface AdaptiveMode {
}
