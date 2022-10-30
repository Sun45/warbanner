package cn.sun45.warbanner.ui.views.character.characterlist.characterlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;
import cn.sun45.warbanner.document.database.source.models.CharacterModel;
import cn.sun45.warbanner.document.statics.charactertype.CharacterScreenType;
import cn.sun45.warbanner.framework.image.ImageRequester;

/**
 * Created by Sun45 on 2021/5/30
 * 角色列表Adapter
 */
public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.Holder> {
    private static final String TAG = "CharacterListAdapter";
    private Context context;

    private CharacterListListener listener;

    private List<CharacterListModel> list;

    public CharacterListAdapter(Context context) {
        this.context = context;
    }

    public void setListener(CharacterListListener listener) {
        this.listener = listener;
    }

    public void setList(List<CharacterListModel> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.characterlist_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CharacterListAdapter.Holder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private CardView card;
        private ImageView icon;
        private TextView name;

        private CharacterListModel model;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharacterModel characterModel = model.getCharacterModel();
                    model.setCharacterScreenType(model.getCharacterScreenType().next());
                    if (listener != null) {
                        listener.changeState(characterModel, model.getCharacterScreenType());
                    }
                    showState();
                }
            });
        }

        public void setData(CharacterListModel characterListModel) {
            model = characterListModel;
            showState();
            CharacterModel characterModel = characterListModel.getCharacterModel();
            String iconUrl = characterModel.getIconUrl();
            if (TextUtils.isEmpty(iconUrl)) {
                icon.setImageBitmap(null);
            } else {
                ImageRequester.request(characterModel.getIconUrl(), R.drawable.ic_character_default).loadRoundImage(icon);
            }
            name.setText(characterModel.getName());
        }

        private void showState() {
            CharacterScreenType characterScreenType = model.getCharacterScreenType();
            card.setCardBackgroundColor(characterScreenType.getBgColor(context));
            name.setTextColor(characterScreenType.getTextColor());
        }
    }
}
