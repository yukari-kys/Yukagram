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

        caption = (
            f"**[{COMMIT_ID[:7]}]"
            f"(https://github.com/yukari-sys/Yukagram/tree/{COMMIT_ID})**\n\n"
            f"{COMMIT_MESSAGE}"
        )

        for i, path in enumerate(files):
            with open(path, "rb") as f:
                print("[bot] sending file:", path)
                await bot.send_document(CHANNEL_ID, f,
                    caption=caption if i == 0 else None,
                    parse_mode="Markdown" if i == 0 else None,
                )

if __name__ == "__main__":
    if os.getenv("SKIP_HEAD_CHECK") or check_is_head():
        asyncio.run(main())
