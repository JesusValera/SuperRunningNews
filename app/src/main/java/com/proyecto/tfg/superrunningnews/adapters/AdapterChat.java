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

package com.proyecto.tfg.superrunningnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Chat;

import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter {

    ArrayList<Chat> mensajes;
    Context context;

    public AdapterChat(ArrayList<Chat> mensajes, Context context) {
        this.mensajes = mensajes;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderChat(LayoutInflater.from(context).inflate(R.layout.item_chat, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HolderChat h = (HolderChat) holder;
        Chat c = mensajes.get(position);
        if (c.isMio()) {
            h.rlMiMsg.setVisibility(View.VISIBLE);
            h.rlSuMsg.setVisibility(View.GONE);
            h.tvMiMsg.setText(c.getTxt());
        } else {
            h.rlMiMsg.setVisibility(View.GONE);
            h.rlSuMsg.setVisibility(View.VISIBLE);
            h.tvSuMsg.setText(c.getTxt());
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    static class HolderChat extends RecyclerView.ViewHolder {
        TextView tvMiMsg, tvSuMsg;
        RelativeLayout rlMiMsg, rlSuMsg;

        public HolderChat(View itemView) {
            super(itemView);
            tvMiMsg = (TextView) itemView.findViewById(R.id.tvMiMsg);
            tvSuMsg = (TextView) itemView.findViewById(R.id.tvSuMsg);

            rlMiMsg = (RelativeLayout) itemView.findViewById(R.id.rlMiMsg);
            rlSuMsg = (RelativeLayout) itemView.findViewById(R.id.rlSuMsg);
        }
    }

}
