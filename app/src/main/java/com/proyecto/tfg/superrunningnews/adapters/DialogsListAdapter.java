/* ******************************************************************************
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

package com.proyecto.tfg.superrunningnews.adapters;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.ViewHolder;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DialogsListAdapter<DIALOG extends IDialog>
        extends RecyclerView.Adapter<DialogsListAdapter.BaseDialogViewHolder> {

    private List<DIALOG> items = new ArrayList<>();
    private int itemLayoutId;
    private Class<? extends BaseDialogViewHolder> holderClass;
    private ImageLoader imageLoader;

    /**
     * For custom list item layout and custom view holder
     *
     * @param itemLayoutId custom list item resource id
     * @param holderClass  custom view holder class
     * @param imageLoader  image loading method
     */
    public DialogsListAdapter(@LayoutRes int itemLayoutId, Class<? extends BaseDialogViewHolder> holderClass,
                              ImageLoader imageLoader) {
        this.itemLayoutId = itemLayoutId;
        this.holderClass = holderClass;
        this.imageLoader = imageLoader;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseDialogViewHolder holder, int position) {
        holder.setImageLoader(imageLoader);
        holder.onBind(items.get(position));
    }

    @Override
    public BaseDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        //create view holder by reflation
        try {
            Constructor<? extends BaseDialogViewHolder> constructor = holderClass.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            return constructor.newInstance(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return size of dialogs list
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /*
    * LISTENERS
    * */
    public interface OnDialogClickListener<DIALOG extends IDialog> {
        void onDialogClick(DIALOG dialog);
    }

    public interface OnDialogLongClickListener<DIALOG extends IDialog> {
        void onDialogLongClick(DIALOG dialog);
    }

    /*
    * HOLDERS
    * */
    public abstract static class BaseDialogViewHolder<DIALOG extends IDialog>
            extends ViewHolder<DIALOG> {

        protected ImageLoader imageLoader;
        protected OnDialogClickListener onDialogClickListener;
        protected OnDialogLongClickListener onLongItemClickListener;
        protected DateFormatter.Formatter datesFormatter;

        public BaseDialogViewHolder(View itemView) {
            super(itemView);
        }

        void setImageLoader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
        }

    }

    public static class DialogViewHolder<DIALOG extends IDialog> extends BaseDialogViewHolder<DIALOG> {
        protected ViewGroup container;
        protected ViewGroup root;
        protected TextView tvName;
        protected TextView tvDate;
        protected ImageView ivAvatar;
        protected ImageView ivLastMessageUser;
        protected TextView tvLastMessage;
        protected TextView tvBubble;
        protected ViewGroup dividerContainer;
        protected View divider;

        public DialogViewHolder(View itemView) {
            super(itemView);
            root = (ViewGroup) itemView.findViewById(R.id.dialogRootLayout);
            container = (ViewGroup) itemView.findViewById(R.id.dialogContainer);
            tvName = (TextView) itemView.findViewById(R.id.dialogName);
            tvDate = (TextView) itemView.findViewById(R.id.dialogDate);
            tvLastMessage = (TextView) itemView.findViewById(R.id.dialogLastMessage);
            tvBubble = (TextView) itemView.findViewById(R.id.dialogUnreadBubble);
            ivLastMessageUser = (ImageView) itemView.findViewById(R.id.dialogLastMessageUserAvatar);
            ivAvatar = (ImageView) itemView.findViewById(R.id.dialogAvatar);
            dividerContainer = (ViewGroup) itemView.findViewById(R.id.dialogDividerContainer);
            divider = itemView.findViewById(R.id.dialogDivider);
        }

        @Override
        public void onBind(final DIALOG dialog) {
            //Set Name
            tvName.setText(dialog.getDialogName());

            //Set Date
            String formattedDate = null;
            Date lastMessageDate = dialog.getLastMessage().getCreatedAt();
            if (datesFormatter != null) formattedDate = datesFormatter.format(lastMessageDate);
            tvDate.setText(formattedDate == null
                    ? getDateString(lastMessageDate)
                    : formattedDate);

            //Set Dialog avatar
            if (imageLoader != null) {
                imageLoader.loadImage(ivAvatar, dialog.getDialogPhoto());
            }

            //Set Last message text
            tvLastMessage.setText(dialog.getLastMessage().getText());

            if (onDialogClickListener != null) {
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onDialogClickListener.onDialogClick(dialog);
                    }
                });
            }

            if (onLongItemClickListener != null) {
                container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onLongItemClickListener.onDialogLongClick(dialog);
                        return true;
                    }
                });
            }
        }

        protected String getDateString(Date date) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
        }

    }
}