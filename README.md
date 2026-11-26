# AntennaPod (Personal Fork)

[![License: GPL v3](https://img.shields.io/github/license/tsahiasher/AntennaPod)](https://www.gnu.org/licenses/gpl-3.0)
[![GitHub Release](https://img.shields.io/github/v/release/tsahiasher/AntennaPod)](https://github.com/tsahiasher/AntennaPod/releases/latest)

This is an unofficial, personal fork of [AntennaPod](https://github.com/AntennaPod/AntennaPod), the open-source podcast manager for Android, focused on streamlined player ergonomics, one-tap playback, cleaner episode/queue management, and background auto-download reliability.

> **Note:** For the official AntennaPod app (Google Play / F-Droid), visit [AntennaPod/AntennaPod](https://github.com/AntennaPod/AntennaPod). Please report issues with this fork [here](https://github.com/tsahiasher/AntennaPod/issues) rather than on the official AntennaPod repository.

<img src="https://raw.githubusercontent.com/AntennaPod/StoreMetadata/main/listings/en-US/graphics/phone-screenshots/00.png" alt="Screenshot 0" height="200"> <img src="https://raw.githubusercontent.com/AntennaPod/StoreMetadata/main/listings/en-US/graphics/phone-screenshots/01.png" alt="Screenshot 1" height="200"> <img src="https://raw.githubusercontent.com/AntennaPod/StoreMetadata/main/listings/en-US/graphics/phone-screenshots/02.png" alt="Screenshot 2" height="200"> <img src="https://raw.githubusercontent.com/AntennaPod/StoreMetadata/main/listings/en-US/graphics/phone-screenshots/03.png" alt="Screenshot 3" height="200"> <img src="https://raw.githubusercontent.com/AntennaPod/StoreMetadata/main/listings/en-US/graphics/phone-screenshots/04.png" alt="Screenshot 4" height="200"> <img src="https://raw.githubusercontent.com/AntennaPod/StoreMetadata/main/listings/en-US/graphics/phone-screenshots/05.png" alt="Screenshot 5" height="200">

## Downloads

APK releases are published on GitHub:
https://github.com/tsahiasher/AntennaPod/releases

> **Installing from official AntennaPod:** Because this fork uses the same package ID (`de.danoeh.antennapod`) signed with a different key, export a database backup (*Settings → Import/Export → Database export*) and uninstall official AntennaPod before installing this APK, then import your backup. Google Play will not overwrite this build with official updates.

### Latest Release (v3.13.0-custom.2)

- Fix: chapter navigation arrows on the player screen now work before the first chapter starts (for episodes whose first chapter begins after `00:00`)
- Episode lists: show the podcast name in small text between the date/size line and the episode title
- Context menu: added **Episode Notes** to the long-press menu across all episode list screens so you can still open show notes directly while background playback continues

### Previous Releases

#### v3.13.0-custom.1

- Based on AntennaPod 3.13.0
- Redesigned audio player screen layout with larger `18sp` timestamps, `116dp` centered Play button, and 2-row controls
- Short-press Skip (keep) vs. long-press Skip (delete, mark as played, preserve play/pause state)
- One-tap play & auto-open full-screen player from Queue, downloaded episodes, and Play buttons
- Automatically mark episodes as played on every downloaded episode deletion
- Background feed refresh & auto-download reliability fixes in `FeedUpdateWorker`
- Keep currently playing episode pinned at top of Queue and removed "Clear queue" from the overflow menu
- Ignore unwanted Bluetooth auto-play when not actively playing, and minimize UI on Back press from the full-screen player

## Custom Changes in This Fork (`fixes` branch)

All changes below are branched from `master` and merged into `fixes`, sorted by order of importance:

1. **Redesigned Audio Player Screen Layout** (`improve_player`):
   - Enlarged timestamp font size (`18sp`) for better readability.
   - Replaced the cramped single-row controls with a spacious 2-row layout (`36dp` horizontal padding) and an enlarged **`116dp`** centered Play/Pause button: Rewind (top-left), Fast Forward (top-right), Play/Pause (center), Playback Speed (bottom-left), and Skip/Delete (bottom-right).
2. **Short-Press Skip vs. Long-Press Delete & Mark as Played** (`skip_button`):
   - Removed the global *"Keep skipped episodes"* setting.
   - **Short press** on Skip keeps the current episode and jumps to the next one.
   - **Long press** on Skip marks the episode as played, removes it from the queue, deletes its downloaded file, and moves to the next episode (preserving play/pause state: starts playing next only if the player was playing).
3. **One-Tap Play & Auto-Open Full-Screen Player** (`improve_player`):
   - In the **Queue** screen, tapping either the episode row or the right button plays the episode and opens the full-screen player.
   - In **Episodes**, **Inbox**, **Downloads**, and **Playback History**, tapping a **downloaded** episode plays it and opens the full-screen player (undownloaded episodes still open show notes).
   - On **any screen**, tapping a Play action button plays the episode and opens the full-screen player.
   - Added **Episode Notes** to the long-press context menu on all episode list screens so show notes remain one long-press away without interrupting background playback.
4. **Mark Episodes as Played on Every Deletion** (`played_delete`):
   - Every downloaded episode deletion (swipe action, long-press menu, multi-select, delete button, or auto-delete) automatically resets playback position and marks the episode as played to keep episode lists clean.
5. **Fix Background Feed Refresh & Auto-Download Reliability** (`auto_download`):
   - Fixed the inverted sign on `JOB_SCHEDULE_TIME_VARIATION` in `FeedUpdateWorker` so hourly background refreshes no longer skip feeds refreshed 60 minutes earlier.
   - Awaited `AutoDownloadManager.autodownloadUndownloadedItems()` inside `FeedUpdateWorker` before returning `Result.success()` so downloads finish enqueueing before the worker exits.
6. **Keep Currently Playing Episode Pinned at Top of Queue** (`improve_queue`):
   - Sorting the queue or adding new episodes keeps the currently playing episode pinned at position #1.
7. **Podcast Name in Episode Lists** (`improve_queue`):
   - Displays the podcast name in small secondary text between the date/size row and the episode title across Queue, Episodes, Downloads, Inbox, and Playback History.
8. **Fix Chapter Navigation Arrows Before First Chapter** (`improve_player`):
   - Allows the Next/Prev chapter arrows on the player screen to work even when playback is before the start timestamp of the first chapter.
9. **Ignore Unwanted Bluetooth Auto-Play** (`improve_player`):
   - Ignores `KEYCODE_MEDIA_PLAY` and Bluetooth playback resumption events when AntennaPod is not actively playing.
10. **Minimize App on Back Press from Full-Screen Player** (`improve_player`):
    - Pressing Back while the full-screen player is expanded minimizes the UI (`moveTaskToBack(true)`) while playback continues in the background.
11. **Remove "Clear queue" from Queue Overflow Menu** (`improve_queue`):
    - Removed the "Clear queue" item from the Queue screen's `⋮` menu to prevent accidentally clearing the queue.

### Redesigned Player Screen Mockup

<img src="docs/player_screen_mockup.svg" alt="Redesigned Player Screen Mockup" width="260">

## Staying close to AntennaPod

This fork is built to follow AntennaPod, not to drift away from it.

**Easy to keep up to date with AntennaPod.** Each change is developed on its own focused feature branch from `master` (`skip_button`, `played_delete`, `auto_download`, `improve_player`, `improve_queue`) and merged into `fixes`, making rebasing onto new upstream AntennaPod releases straightforward:

- Upstream AntennaPod code is touched only where a change has to hook in, with minimal, targeted diffs and no reorganizing, renaming, or reformatting.
- Because each feature lives on a separate branch, individual changes can be dropped, adjusted, or submitted upstream independently.

**Your data stays compatible with the official app.**

- AntennaPod's database schema and version number are never modified.
- A database backup exported from official AntennaPod imports into this fork as is, and a backup exported from this fork imports cleanly back into official AntennaPod.
- Updating this fork never requires a fresh start—all subscriptions, queue order, playback history, and downloaded episodes stay intact.

## License

AntennaPod (and this fork) is licensed under the GNU General Public License (GPL-3.0). You can find the license text in the [LICENSE](LICENSE) file.

## Building AntennaPod

You can build AntennaPod just like any other Android project. Refer to the [instructions](https://github.com/AntennaPod/AntennaPod/blob/develop/CONTRIBUTING.md) for more details.

