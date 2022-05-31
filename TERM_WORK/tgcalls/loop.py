import os

from pytgcalls.exceptions import AlreadyJoinedError
from pytgcalls.types import InputStream, InputVideoStream, VideoParameters


async def start_video_loop(call, group_id, video='video.raw'):
    while not os.path.exists(video):
        print('Video file not found :(')
        return
    steam = InputVideoStream(
        video,
        VideoParameters(
            width=640,
            height=360,
            frame_rate=25,
        ),
    )
    try:
        await call.join_group_call(
            group_id,
            InputStream(
                stream_video=steam
            ),
        )
    except AlreadyJoinedError:
        await call.change_stream(
            group_id,
            InputStream(
                stream_video=steam
            ),
        )
