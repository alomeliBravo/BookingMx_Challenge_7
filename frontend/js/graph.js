/**
 * @fileoverview Graph data structures and algorithms for representing nearby cities.
 * @description
 * Implements a simple undirected weighted graph with utility functions
 * to validate datasets, build graphs, and find nearby cities.
 * 
 * All functions are pure and side-effect-free (except class mutations),
 * which makes them ideal for Jest unit testing.
 */

/**
 * Represents an undirected weighted graph of cities.
 * Each city is connected by edges with distance values in kilometers.
 */
export class Graph {
  /**
   * Creates an empty graph.
   * @constructor
   */
  constructor() {
    /**
     * Adjacency list mapping each city to an array of connections.
     * @type {Map<string, Array<{to: string, distance: number}>>}
     */
    this.adj = new Map();
  }

  /**
   * Adds a city (node) to the graph if it doesn't already exist.
   * 
   * @param {string} name - Name of the city to add.
   * @throws {Error} If the city name is invalid (not a non-empty string).
   * 
   * @example
   * const g = new Graph();
   * g.addCity("Guadalajara");
   */
  addCity(name) {
    if (!name || typeof name !== "string") throw new Error("Invalid city name");
    if (!this.adj.has(name)) this.adj.set(name, []);
  }

  /**
   * Adds an undirected edge between two cities with the given distance.
   * 
   * @param {string} from - Starting city name.
   * @param {string} to - Destination city name.
   * @param {number} distanceKm - Distance in kilometers between the cities.
   * @throws {Error} If the cities don't exist or distance is invalid.
   * 
   * @example
   * g.addCity("A");
   * g.addCity("B");
   * g.addEdge("A", "B", 50);
   */
  addEdge(from, to, distanceKm) {
    if (!this.adj.has(from) || !this.adj.has(to)) throw new Error("Unknown city");
    if (!Number.isFinite(distanceKm) || distanceKm < 0) throw new Error("Invalid distance");
    this.adj.get(from).push({ to, distance: distanceKm });
    this.adj.get(to).push({ to: from, distance: distanceKm }); // undirected
  }

  /**
   * Retrieves the neighbors (connected cities) of a given city.
   * 
   * @param {string} city - City name whose neighbors to retrieve.
   * @returns {Array<{to: string, distance: number}>} Array of neighboring cities and distances.
   * @throws {Error} If the city does not exist in the graph.
   * 
   * @example
   * const neighbors = g.neighbors("Guadalajara");
   * console.log(neighbors);
   */
  neighbors(city) {
    if (!this.adj.has(city)) throw new Error("Unknown city");
    return [...this.adj.get(city)];
  }
}

/**
 * Validates a dataset of cities and edges before graph creation.
 * 
 * @param {{ cities: string[], edges: Array<{from: string, to: string, distance: number}> }} data
 *   The dataset containing cities and edges.
 * @returns {{ ok: boolean, reason?: string }} Validation result object.
 * 
 * @example
 * const result = validateGraphData({ cities: ["A", "B"], edges: [{from: "A", to: "B", distance: 10}] });
 * if (!result.ok) console.error(result.reason);
 */
export function validateGraphData({ cities, edges }) {
  if (!Array.isArray(cities) || !Array.isArray(edges))
    return { ok: false, reason: "cities/edges must be arrays" };
  const citySet = new Set(cities);
  if (citySet.size !== cities.length) return { ok: false, reason: "duplicate cities" };
  for (const c of cities)
    if (typeof c !== "string" || !c.trim()) return { ok: false, reason: "invalid city entry" };
  for (const e of edges) {
    const { from, to, distance } = e ?? {};
    if (!citySet.has(from) || !citySet.has(to))
      return { ok: false, reason: "edge references unknown city" };
    if (!Number.isFinite(distance) || distance < 0)
      return { ok: false, reason: "invalid distance" };
  }
  return { ok: true };
}

/**
 * Builds a graph from arrays of cities and edges.
 * 
 * @param {string[]} cities - Array of city names.
 * @param {Array<{from: string, to: string, distance: number}>} edges - Array of edge objects.
 * @returns {Graph} A new {@link Graph} instance populated with the provided data.
 * 
 * @example
 * const g = buildGraph(["A", "B"], [{from: "A", to: "B", distance: 100}]);
 */
export function buildGraph(cities, edges) {
  const g = new Graph();
  for (const c of cities) g.addCity(c);
  for (const { from, to, distance } of edges) g.addEdge(from, to, distance);
  return g;
}

/**
 * Gets cities that are directly connected to the given destination
 * and within the specified maximum distance.
 * 
 * @param {Graph} graph - The graph instance to query.
 * @param {string} destination - The destination city.
 * @param {number} [maxDistanceKm=250] - Maximum distance (in km) to consider a nearby city.
 * @returns {Array<{city: string, distance: number}>} List of nearby cities within the distance limit.
 * 
 * @example
 * const nearby = getNearbyCities(graph, "Guadalajara", 100);
 * console.log(nearby);
 */
export function getNearbyCities(graph, destination, maxDistanceKm = 250) {
  if (!(graph instanceof Graph)) throw new Error("graph must be Graph");
  if (typeof destination !== "string" || !graph.adj.has(destination)) return [];
  const neighbors = graph.neighbors(destination);
  return neighbors
    .filter(n => n.distance <= maxDistanceKm)
    .sort((a, b) => a.distance - b.distance)
    .map(n => ({ city: n.to, distance: n.distance }));
}

/**
 * Example dataset of cities and distances (in kilometers).
 * Useful for demos, tests, or initial app states.
 * 
 * @type {{cities: string[], edges: Array<{from: string, to: string, distance: number}>}}
 */
export const sampleData = {
  cities: [
    "Guadalajara", "Tlaquepaque", "Zapopan", "Tepatitlán", "Lagos de Moreno", "Tala", "Tequila"
  ],
  edges: [
    { from: "Guadalajara", to: "Zapopan", distance: 12 },
    { from: "Guadalajara", to: "Tlaquepaque", distance: 10 },
    { from: "Guadalajara", to: "Tepatitlán", distance: 78 },
    { from: "Guadalajara", to: "Tequila", distance: 60 },
    { from: "Zapopan", to: "Tala", distance: 35 },
    { from: "Tepatitlán", to: "Lagos de Moreno", distance: 85 }
  ]
};