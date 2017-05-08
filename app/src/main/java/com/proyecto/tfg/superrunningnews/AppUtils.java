/*******************************************************************************
 * Copyright 2016 stfalcon.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.proyecto.tfg.superrunningnews;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class AppUtils {

        public static void showToast(Context context, @StringRes int text, boolean isLong) {
            showToast(context, context.getString(text), isLong);
        }

        public static void showToast(Context context, String text, boolean isLong) {
            Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        }
}