export const API_ROOT = 'https://around-75015.appspot.com/api/v1';
export const TOKEN_KEY = 'TOKEN_KEY';

// route 1: Manhattan College -> Empire state building -> Columbia University -> Statue of Liberty National Monument
// route 2: MoMA -> Statue of Liberty National Monument
// route 3: Central Park -> Flushing
export const PLACE_IDS = [
    { planId: 0, number: 4, placeIds: ["ChIJlfhnNaXzwokRQs-EpgsQ970","ChIJtcaxrqlZwokRfwmmibzPsTU", "ChIJyQ3Tlj72wokRUCflR_kzeVc", "ChIJPTacEpBQwokRKwIlDXelxkA"] },
    { planId: 1, number: 2, placeIds: ["ChIJmQJIxlVYwokRLgeuocVOGVU", "ChIJPTacEpBQwokRKwIlDXelxkA"] },
    { planId: 2, number: 2, placeIds: ["ChIJ4zGFAZpYwokRGUGph3Mf37k", "ChIJP2PATQVgwokRHih0tNEk7Po"] }
];

// Use your own google map API key
export const MAP_API_KEY = 'blablabla';
export const MAP_LIBRARIES = 'geometry,drawing,places';
