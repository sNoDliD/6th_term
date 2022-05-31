from pyrogram import Client, filters
from pytgcalls import PyTgCalls
from pytgcalls import idle
from pytgcalls.types import Update

import player
from loop import start_video_loop
from youtube import start_video_youtube

app = Client(
    'client',
    api_id=1232386,
    api_hash='ac7490d60e1596845b62534a3ad822b0',
    phone_number='+380954958009'
)
call_py = PyTgCalls(app)


@call_py.on_kicked()
async def kicked_handler(_, chat_id: int):
    print(f'Kicked from {chat_id}')


@call_py.on_stream_end()
async def stream_end_handler(_, update: Update):
    if Update.default(update)["_"] == 'StreamVideoEnded':
        await start_video_loop(call_py, update.chat_id)


@call_py.on_participants_change()
async def participant_handler(_, update: Update):
    print("Participant data changed:", update)


@app.on_message(filters.regex(r'!volume (\d{1,3})'))
async def change_volume_handler(_, message):
    volume = int(message.matches[0].group(1))

    await call_py.change_volume_call(
        message.chat.id,
        volume,
    )


@app.on_message(filters.regex('!play'))
async def play_handler(_, message):
    await player.play_audio(call_py, message.chat.id)


@app.on_message(filters.regex('!pause'))
async def pause_handler(_, message):
    await player.pause(call_py, message.chat.id)


@app.on_message(filters.regex('!resume'))
async def resume_handler(_: Client, message):
    await player.resume(call_py, message.chat.id)


@app.on_message(filters.regex('!leave'))
async def leave_handler(_, message):
    await player.leave(call_py, message.chat.id)


@app.on_message(filters.regex('!video_loop'))
async def start_loop_handler(_, message):
    await start_video_loop(call_py, message.chat.id)


@app.on_message(filters.regex('!start_youtube ([^ ]+)'))
async def start_youtube_handler(_, message):
    url = message.matches[0].group(1)
    await start_video_youtube(call_py, message.chat.id, url)


@app.on_message(filters.regex('!help'))
async def stop_handler(client: Client, message):
    await client.send_message(message.chat.id, """!play
!pause
!resume
!volume 20
!start_youtube https://www.youtube.com/watch?v=F1fl60ypdLs
!video_loop
!leave""",
                              reply_to_message_id=message.id)


call_py.start()
idle()
