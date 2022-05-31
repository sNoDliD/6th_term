import os

from pytgcalls import StreamType
from pytgcalls.exceptions import AlreadyJoinedError
from pytgcalls.types.input_stream import InputAudioStream
from pytgcalls.types.input_stream import InputStream


async def play_audio(call, chat_id):
    file = './audio.raw'
    while not os.path.exists(file):
        print("Audio file doesn't exists")
        return
    input_stream = InputStream(
        InputAudioStream(
            file,
        ),
    )
    try:
        await call.join_group_call(
            chat_id,
            input_stream,
            stream_type=StreamType().local_stream,
        )
    except AlreadyJoinedError:
        await call.change_stream(
            chat_id,
            input_stream
        )


async def pause(call, chat_id):
    await call.pause_stream(
        chat_id,
    )


async def resume(call, chat_id):
    await call.resume_stream(
        chat_id,
    )


async def leave(call, chat_id):
    await call.leave_group_call(
        chat_id,
    )
