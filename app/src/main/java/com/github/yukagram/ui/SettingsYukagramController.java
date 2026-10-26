package com.github.yukagram.ui;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.github.yukagram.YuSettings;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.tool.UI;
import org.thunderdog.challegram.ui.ListItem;
import org.thunderdog.challegram.ui.RecyclerViewController;
import org.thunderdog.challegram.ui.SettingsAdapter;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.List;

public class SettingsYukagramController extends RecyclerViewController<Void> implements View.OnClickListener {
  public SettingsYukagramController(Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override
  public int getId () {
    return R.id.controller_yukagramSettings;
  }

  private SettingsAdapter adapter;

  @Override
  protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    this.adapter = new SettingsAdapter(this) {
      @Override
      protected void setValuedSetting(ListItem item, SettingView view, boolean isUpdate) {
        int itemId = item.getId();

        if (itemId == R.id.btn_hideConnectionStatusBar) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isConnectionStatusBarHidden(), isUpdate);
        } else if (itemId == R.id.btn_hideCameraButton) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isHideCameraButton(), isUpdate);
        } else if (itemId == R.id.btn_moveArchiveToSidebar) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isMoveArchiveToSidebar(), isUpdate);
        } else if (itemId == R.id.btn_moveCallsToSidebar) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isMoveCallsToSidebar(), isUpdate);
        } else if (itemId == R.id.btn_hidePhoneNumbers) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isHidePhoneNumbers(), isUpdate);
        } else if (itemId == R.id.btn_hideSidebarButtonInviteFriends) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isHideSidebarButtonInviteFriends(), isUpdate);
        } else if (itemId == R.id.btn_hideSidebarButtonNightMode) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isHideSidebarButtonNightMode(), isUpdate);
        } else if (itemId == R.id.btn_hideSidebarButtonHelp) {
          view.getToggler().setRadioEnabled(YuSettings.getInstance().isHideSidebarButtonHelp(), isUpdate);
        }
      }
    };
    List<ListItem> items = adapter.getItems();

    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.yu_Annoyances));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideConnectionStatusBar, 0, R.string.yu_HideConnectionStatusBar));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.yu_Inputbar));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideCameraButton, 0, R.string.yu_HideCameraButton));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.yu_HideCameraButtonDescription));

    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.yu_Sidebar));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_moveArchiveToSidebar, 0, R.string.yu_MoveArchiveToSidebar));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_moveCallsToSidebar, 0, R.string.yu_MoveCallsToSidebar));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideSidebarButtonInviteFriends, 0, R.string.yu_HideSidebarButtonInviteFriends));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideSidebarButtonNightMode, 0, R.string.yu_HideSidebarButtonNightMode));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hideSidebarButtonHelp, 0, R.string.yu_HideSidebarButtonHelp));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.yu_Privacy));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hidePhoneNumbers, 0, R.string.yu_HidePhoneNumbers));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onClick(View v) {
    int viewId = v.getId();

    boolean needToRestart = false;

    if (viewId == R.id.btn_hideConnectionStatusBar) {
      YuSettings.getInstance().setConnectionStatusBarHidden(adapter.toggleView(v));
      needToRestart = true;
    } else if (viewId == R.id.btn_hideCameraButton) {
      YuSettings.getInstance().setHideCameraButton(adapter.toggleView(v));
    } else if (viewId == R.id.btn_moveArchiveToSidebar) {
      YuSettings.getInstance().setMoveArchiveToSidebar(adapter.toggleView(v));
      needToRestart = true;
    } else if (viewId == R.id.btn_moveCallsToSidebar) {
      YuSettings.getInstance().setMoveCallsToSidebar(adapter.toggleView(v));
      needToRestart = true;
    } else if (viewId == R.id.btn_hidePhoneNumbers) {
      YuSettings.getInstance().setHidePhoneNumbers(adapter.toggleView(v));
    } else if (viewId == R.id.btn_hideSidebarButtonInviteFriends) {
      YuSettings.getInstance().setHideSidebarButtonInviteFriends(adapter.toggleView(v));
      needToRestart = true;
    } else if (viewId == R.id.btn_hideSidebarButtonNightMode) {
      YuSettings.getInstance().setHideSidebarButtonNightMode(adapter.toggleView(v));
      needToRestart = true;
    } else if (viewId == R.id.btn_hideSidebarButtonHelp) {
      YuSettings.getInstance().setHideSidebarButtonHelp(adapter.toggleView(v));
      needToRestart = true;
    }

    if (needToRestart) {
      UI.showToast(R.string.yu_RestartIsNeeded, Toast.LENGTH_SHORT);
    }
  }

  @Override
  public void destroy () {
    super.destroy();
  }

  @Override
  public CharSequence getName () {
    return Lang.getString(R.string.yu_YukagramSettings);
  }
}
