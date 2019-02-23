export const API_ROOT = 'https://around-75015.appspot.com/api/v1';
export const TOKEN_KEY = 'TOKEN_KEY';

// route 1: Manhattan College -> Empire state building -> Columbia University -> Statue of Liberty National Monument
// route 2: MoMA -> Statue of Liberty National Monument
// route 3: Central Park -> Flushing
export const PLACE_IDS = [
    { plan_id: 0, number: 4, place_ids: ["ChIJN3MJ6pRYwokRiXg91flSP8Y","ChIJtcaxrqlZwokRfwmmibzPsTU", "ChIJi4hYtB32wokR1Npx_Tv7phk", "ChIJPTacEpBQwokRKwIlDXelxkA", "ChIJcWnnWiz0wokRCB6aVdnDQEk"] },
    { plan_id: 1, number: 2, place_ids: ["ChIJmQJIxlVYwokRLgeuocVOGVU", "ChIJPTacEpBQwokRKwIlDXelxkA"] },
    { plan_id: 2, number: 2, place_ids: ["ChIJ4zGFAZpYwokRGUGph3Mf37k", "ChIJP2PATQVgwokRHih0tNEk7Po"] }
];

// Use your own google map API key
export const MAP_API_KEY = 'AIzaSyCaQvK08ZBh3obNQ8TdDrpKzWYYGopZQyI';
export const MAP_LIBRARIES = 'geometry,drawing,places';
