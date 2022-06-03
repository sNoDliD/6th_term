from heapq import heappush, heapify, heappop

from tree import Tree
from data_types import Segment, Point, PointType


def find_intersections(*segments: Segment):
    result = []
    events = []
    tree = Tree()
    for s in segments:
        heappush(events, s.start)
        heappush(events, s.end)

    while events:
        p: Point = heappop(events)

        tree.sweep_line = p.x
        if p.point_type is PointType.start:
            # print('add', p.segment)
            s = tree.insert(p.segment)
            left, right = tree.get_neighbors(s)
            _remove_intersect(left, right, p.x, events)
            _add_intersect(s, left, p.x, events)
            _add_intersect(s, right, p.x, events)

        elif p.point_type is PointType.end:
            # print('rm', p.segment)
            s = p.segment
            left, right = tree.get_neighbors(s)
            tree.remove(s)
            _add_intersect(left, right, p.x, events)

        elif p.point_type is PointType.cross:
            # print('!cross', p)
            result.append(p)
            s1 = p.segment
            s2 = p.cross_segment
            left, _ = tree.get_neighbors(s1)
            _, right = tree.get_neighbors(s2)
            _remove_intersect(right, s2, p.x, events)
            _remove_intersect(left, s1, p.x, events)
            _add_intersect(right, s1, p.x, events)
            _add_intersect(left, s2, p.x, events)
            tree.swap(s1, s2)

    return result


def _add_intersect(seg1, seg2, sx, events):
    if seg1 is None or seg2 is None:
        return
    cross_point = seg1.intersection(seg2)
    if cross_point and cross_point.x > sx:
        # print('future cross?', cross_point)
        cross_point.set_cross_segment(seg1, seg2)
        heappush(events, cross_point)


def _remove_intersect(seg1, seg2, sx, events):
    if seg1 is None or seg2 is None:
        return
    cross_point = seg1.intersection(seg2)
    if cross_point and cross_point.x > sx:
        events.remove(cross_point)
        heapify(events)
