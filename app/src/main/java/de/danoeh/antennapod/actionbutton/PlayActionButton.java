package de.danoeh.antennapod.actionbutton;

import android.content.Context;
import android.util.Log;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import de.danoeh.antennapod.R;
import de.danoeh.antennapod.activity.MainActivity;
import de.danoeh.antennapod.playback.service.PlaybackService;
import de.danoeh.antennapod.playback.service.PlaybackServiceStarter;
import de.danoeh.antennapod.storage.database.DBWriter;
import de.danoeh.antennapod.event.FeedItemEvent;
import de.danoeh.antennapod.event.MessageEvent;
import de.danoeh.antennapod.model.feed.FeedItem;
import de.danoeh.antennapod.model.feed.FeedMedia;
import de.danoeh.antennapod.model.playback.MediaType;
import org.greenrobot.eventbus.EventBus;

import java.util.Collections;

public class PlayActionButton extends ItemActionButton {
    private static final String TAG = "PlayActionButton";

    public PlayActionButton(FeedItem item) {
        super(item);
    }

    @Override
    @StringRes
    public int getLabel() {
        return R.string.play_label;
    }

    @Override
    @DrawableRes
    public int getDrawable() {
        return R.drawable.ic_play_24dp;
    }

    @Override
    public void onClick(Context context) {
        FeedMedia media = item.getMedia();
        if (media == null) {
            return;
        }
        if (!media.fileExists()) {
            Log.i(TAG, "Missing episode. Will update the database now.");
            media.setDownloaded(false, 0);
            media.setLocalFileUrl(null);
            DBWriter.setMediaDownloadInformation(media);
            EventBus.getDefault().post(new FeedItemEvent(Collections.singletonList(media.getItem()), false));
            EventBus.getDefault().post(new MessageEvent(context.getString(R.string.error_file_not_found)));
            return;
        }
        new PlaybackServiceStarter(context, media)
                .callEvenIfRunning(true)
                .start();

        if (media.getMediaType() == MediaType.VIDEO) {
            context.startActivity(PlaybackService.getPlayerActivityIntent(context, media));
        } else if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.setPlayerVisible(true);
            mainActivity.getBottomSheet().setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}
