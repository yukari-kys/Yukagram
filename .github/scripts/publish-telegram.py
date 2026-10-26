#!/usr/bin/env python3

import os
import glob
import asyncio
import subprocess
from telegram import Bot, InputMediaDocument

BOT_TOKEN = os.getenv("TELEGRAM_BOT_TOKEN")
COMMIT_ID = os.getenv("GIT_COMMIT_ID")
COMMIT_MESSAGE = os.getenv("GIT_COMMIT_MESSAGE")
CHANNEL_ID = "@yukagram"

def check_is_head():
    subprocess.run(["git", "fetch", "origin"], check=True)
    main_commit = subprocess.check_output(
        ["git", "rev-parse", "origin/main"],
        text=True,
    ).strip()

    res = main_commit == COMMIT_ID
    if not res:
        print("[git] not on head:", main_commit, "yours:", COMMIT_ID)
    return res

async def main():
    async with Bot(BOT_TOKEN) as bot:
        files = glob.glob("./*.apk")
        files = [i for i in files if not i.endswith("-debug.apk")]

        caption = (
            f"**[{COMMIT_ID[:7]}]"
            f"(https://github.com/yukari-sys/Yukagram/tree/{COMMIT_ID})**\n\n"
            f"{COMMIT_MESSAGE}"
        )

        media = []

        for i, path in enumerate(files):
            print("[bot] adding file:", path)
            media.append(InputMediaDocument(
                media=open(path, "rb"),
                filename=os.path.basename(path)
            ))

        print("[bot] sending files")
        await bot.send_media_group(
            chat_id=CHANNEL_ID,
            media=media,
            caption=caption,
            parse_mode="Markdown",
        )

if __name__ == "__main__":
    if os.getenv("SKIP_HEAD_CHECK") or check_is_head():
        asyncio.run(main())
