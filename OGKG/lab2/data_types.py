import heapq
import itertools


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return f'<Point {round(self.x, 2)},{round(self.y, 2)}>'


class Event:
    def __init__(self, x, p, a):
        self.x = x
        self.p = p
        self.a = a
        self.valid = True


class Arc:
    def __init__(self, p, a=None, b=None):
        self.p = p
        self.pprev = a
        self.pnext = b
        self.e = None
        self.s0 = None
        self.s1 = None

    def __str__(self):
        return f'<Arc {self.p}, {[self.e, self.s0, self.s1]}>'


class Segment:
    def __init__(self, p):
        self.start = p
        self.end = None

    def finish(self, p):
        if self.done:
            return
        self.end = p

    @property
    def done(self):
        return self.end is not None

    def __repr__(self):
        return f"<Segment {self.start}-{self.end}>"


class PriorityQueue:
    def __init__(self):
        self.pq = []
        self.entry_finder = {}
        self.counter = itertools.count()

    def push(self, item):
        if item in self.entry_finder:
            return
        count = next(self.counter)
        entry = [item.x, count, item]
        self.entry_finder[item] = entry
        heapq.heappush(self.pq, entry)

    def remove_entry(self, item):
        entry = self.entry_finder.pop(item)
        entry[-1] = None

    def pop(self):
        while self.pq:
            priority, count, item = heapq.heappop(self.pq)
            if item:
                del self.entry_finder[item]
                return item
        raise KeyError('pop from an empty priority queue')

    def top(self):
        while self.pq:
            priority, count, item = heapq.heappop(self.pq)
            if item:
                del self.entry_finder[item]
                self.push(item)
                return item
        raise KeyError('top from an empty priority queue')

    def empty(self):
        return not self.pq
