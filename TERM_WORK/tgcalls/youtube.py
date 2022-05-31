import asyncio

from pytgcalls import StreamType
from pytgcalls.exceptions import AlreadyJoinedError
from pytgcalls.types import AudioVideoPiped, HighQualityAudio, \
    HighQualityVideo


async def get_youtube_stream(link):
    proc = await asyncio.create_subprocess_exec(
        'youtube-dl',
        '-g',
        '-f',
        'best[height<=?480][width<=?850]',
        link,
        stdout=asyncio.subprocess.PIPE,
        stderr=asyncio.subprocess.PIPE,
    )
    stdout, stderr = await proc.communicate()
    return stdout.decode().split('\n')[0]


async def start_video_youtube(call, group_id, link):
    remote = await get_youtube_stream(link)
    input_stream = AudioVideoPiped(
        remote,
        HighQualityAudio(),
        HighQualityVideo(),
    )
    try:
        await call.join_group_call(
            group_id,
            input_stream,
            stream_type=StreamType().pulse_stream,
        )
    except AlreadyJoinedError:
        await call.change_stream(
            group_id,
            input_stream
        )
