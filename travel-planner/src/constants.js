export const API_ROOT = 'https://around-75015.appspot.com/api/v1';
export const TOKEN_KEY = 'TOKEN_KEY';

// route 1: Empire state building -> Columbia University
// route 2: MoMA -> Statue of Liberty National Monument
// route 3: Central Park -> Flushing
export const PLACE_IDS = [
    { placeIds: ["ChIJtcaxrqlZwokRfwmmibzPsTU", "ChIJyQ3Tlj72wokRUCflR_kzeVc"] },
    { placeIds: ["ChIJmQJIxlVYwokRLgeuocVOGVU", "ChIJPTacEpBQwokRKwIlDXelxkA"] },
    { placeIds: ["ChIJ4zGFAZpYwokRGUGph3Mf37k", "ChIJP2PATQVgwokRHih0tNEk7Po"] }
];

// Use your own google map API key
export const MAP_API_KEY = 'blablabla';
export const MAP_LIBRARIES = 'geometry,drawing,places';
