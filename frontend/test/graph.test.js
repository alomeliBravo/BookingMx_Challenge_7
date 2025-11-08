import {
    Graph,
    validateGraphData,
    buildGraph,
    getNearbyCities,
    sampleData
} from '../js/graph.js';

describe('Graph class', () => {
    let g;
    beforeEach(() => {
        g = new Graph();
    });

    test('addCity adds a new city', () => {
        g.addCity('Guadalajara');
        expect(g.adj.has('Guadalajara')).toBe(true);
        expect(g.adj.get('Guadalajara')).toEqual([]);
    });

    test('addCity ignores duplicates', () => {
        g.addCity('A');
        g.addCity('A');
        expect(g.adj.size).toBe(1);
    });

    test('addCity throws on invalid name', () => {
        expect(() => g.addCity('')).toThrow('Invalid city name');
        expect(() => g.addCity(null)).toThrow('Invalid city name');
    });

    test('addEdge creates undirected edges', () => {
        g.addCity('A');
        g.addCity('B');
        g.addEdge('A', 'B', 10);
        expect(g.adj.get('A')).toEqual([{ to: 'B', distance: 10 }]);
        expect(g.adj.get('B')).toEqual([{ to: 'A', distance: 10 }]);
    });

    test('addEdge throws if cities unknown or distance invalid', () => {
        g.addCity('A');
        expect(() => g.addEdge('A', 'B', 10)).toThrow('Unknown city');
        g.addCity('B');
        expect(() => g.addEdge('A', 'B', -5)).toThrow('Invalid distance');
        expect(() => g.addEdge('A', 'B', NaN)).toThrow('Invalid distance');
    });

    test('neighbors returns adjacent nodes', () => {
        g.addCity('A');
        g.addCity('B');
        g.addEdge('A', 'B', 5);
        expect(g.neighbors('A')).toEqual([{ to: 'B', distance: 5 }]);
    });

    test('neighbors throws on unknown city', () => {
        expect(() => g.neighbors('X')).toThrow('Unknown city');
    });
});

describe('validateGraphData', () => {
    test('valid data returns ok:true', () => {
        const data = { cities: ['A', 'B'], edges: [{ from: 'A', to: 'B', distance: 5 }] };
        expect(validateGraphData(data)).toEqual({ ok: true });
    });

    test('rejects non-array inputs', () => {
        expect(validateGraphData({ cities: null, edges: [] }).ok).toBe(false);
        expect(validateGraphData({ cities: [], edges: 'x' }).ok).toBe(false);
    });

    test('rejects duplicate or invalid cities', () => {
        const d1 = { cities: ['A', 'A'], edges: [] };
        const d2 = { cities: [''], edges: [] };
        expect(validateGraphData(d1).ok).toBe(false);
        expect(validateGraphData(d2).ok).toBe(false);
    });

    test('rejects edges with unknown cities or invalid distance', () => {
        const d1 = { cities: ['A'], edges: [{ from: 'A', to: 'B', distance: 5 }] };
        const d2 = { cities: ['A', 'B'], edges: [{ from: 'A', to: 'B', distance: -5 }] };
        expect(validateGraphData(d1).ok).toBe(false);
        expect(validateGraphData(d2).ok).toBe(false);
    });
});

describe('buildGraph', () => {
    test('creates graph with correct connections', () => {
        const cities = ['A', 'B'];
        const edges = [{ from: 'A', to: 'B', distance: 10 }];
        const g = buildGraph(cities, edges);
        expect(g.adj.size).toBe(2);
        expect(g.neighbors('A')).toEqual([{ to: 'B', distance: 10 }]);
    });
});

describe('getNearbyCities', () => {
    let g;
    beforeEach(() => {
        g = new Graph();
        ['A', 'B', 'C'].forEach(c => g.addCity(c));
        g.addEdge('A', 'B', 100);
        g.addEdge('A', 'C', 300);
    });

    test('returns nearby cities sorted by distance', () => {
        const result = getNearbyCities(g, 'A', 250);
        expect(result).toEqual([{ city: 'B', distance: 100 }]);
    });

    test('returns empty for destination not in graph', () => {
        expect(getNearbyCities(g, 'Z')).toEqual([]);
    });

    test('returns empty if graph invalid', () => {
        expect(() => getNearbyCities({}, 'A')).toThrow('graph must be Graph');
    });
});

describe('sampleData', () => {
    test('sampleData structure looks valid', () => {
        const { cities, edges } = sampleData;
        expect(Array.isArray(cities)).toBe(true);
        expect(Array.isArray(edges)).toBe(true);
        expect(validateGraphData(sampleData).ok).toBe(true);
    });
});
