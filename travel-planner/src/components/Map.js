import React from "react";
import {API_ROOT, MAP_API_KEY, MAP_LIBRARIES} from "../constants";
import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import {Icon} from "antd";
import {Loading} from "./Loading";

const {compose, withProps, lifecycle, withStateHandlers} = require("recompose");
const {
    withScriptjs,
    withGoogleMap,
    GoogleMap,
    DirectionsRenderer,
    Marker
} = require("react-google-maps");
const {InfoBox} = require("react-google-maps/lib/components/addons/InfoBox");
/*global google*/

// Define the map area
const MapWithADirectionsRenderer = compose(
    withProps({
        googleMapURL: `https://maps.googleapis.com/maps/api/js?key=${MAP_API_KEY}&v=3.exp&libraries=${MAP_LIBRARIES}`,
        loadingElement: <div style={{height: `100%`}}/>,
        containerElement: <div style={{height: `450px`, width: `450px`}}/>,
        mapElement: <div style={{height: `100%`}}/>
    }),
    withStateHandlers(() => ({
        isOpen: [false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false]
    }), {
        onToggleOpen: ({isOpen}) => (index, name) => {
            console.log({
                user_id: localStorage.getItem('user_id'),
                place_id: name,
                event: "click"
            });
            return ({
            isOpen: [...isOpen.slice(0,index), !isOpen[index], ...isOpen.slice(index + 1)],
        });
        }
    }),
    withScriptjs,
    withGoogleMap,
    lifecycle({
        componentDidMount() {
            this.setState({
                directionService: new google.maps.DirectionsService(),
                options: {
                    draggable: false,
                    suppressInfoWindows: true,
                    suppressMarkers: true,
                }
            });
        },
        componentDidUpdate(prevProps, prevState) {
            if (
                this.props.placeIds === undefined ||
                this.props.placeIds === null ||
                this.props.placeIds.length < 2
            ) {
                if (this.state.directions !== undefined) {
                    this.setState({
                        directions: undefined,
                        placeInfos: undefined
                    });
                }
                return;
            }
            // if the place ids changed, resend the request to MAP API
            if (prevProps.placeIds !== this.props.placeIds) {
                // generate the way points
                let len = this.props.placeIds.length;
                let wypts = [];
                for (let i = 1; i < len - 1; i++) {
                    wypts.push({
                        location: {placeId: this.props.placeIds[i]},
                        stopover: true
                    });
                }
                this.state.directionService.route(
                    {
                        origin: {placeId: this.props.placeIds[0]},
                        waypoints: wypts,
                        destination: {placeId: this.props.placeIds[len - 1]},
                        travelMode: google.maps.TravelMode.DRIVING
                    },
                    (result, status) => {
                        if (status === google.maps.DirectionsStatus.OK) {
                            // console.log(result);
                            // console.log(this.props.placeIds);
                            let placeInfos = [];
                            placeInfos.push({
                                placeNames: this.props.placeNames[0],
                                placeGeos: {
                                    lat: result.routes[0].legs[0].start_location.lat(),
                                    lng: result.routes[0].legs[0].start_location.lng()
                                }
                            });
                            // console.log(result.routes[0].legs[0].start_location.lat());
                            for (let i = 0; i < this.props.placeNames.length - 1; i++) {
                                placeInfos.push({
                                    placeNames: this.props.placeNames[i + 1],
                                    placeGeos: {
                                        lat: result.routes[0].legs[i].end_location.lat(),
                                        lng: result.routes[0].legs[i].end_location.lng()
                                    }
                                })
                            }
                            this.setState({
                                directions: result,
                                placeInfos
                            });
                        } else {
                            console.error(`error fetching directions ${status}`);
                        }
                    }
                );
            }
        },
        componentWillUnmount() {
            this.setState();
        }
    })
)(props => (
    <GoogleMap defaultZoom={7} defaultCenter={{lat: 41.85, lng: -87.65}}>
        {props.directions && <DirectionsRenderer directions={props.directions} options={props.options}/>}
        {props.placeInfos && props.placeInfos.map((placeInfo, index) => {
            return (
                <Marker
                    position={placeInfo.placeGeos}
                    onClick={() => {props.onToggleOpen(index, placeInfo.placeNames)}}
                    label={`${index + 1}`}
                    key={`marker-${index}`}
                >
                    {props.isOpen[index] && <InfoBox
                        onCloseClick={() => {props.onToggleOpen(index)}}
                        options={{closeBoxURL: ``, enableEventPropagation: true}}
                    >
                        <div className='info-window'>
                            <div>
                                {placeInfo.placeNames}
                            </div>
                            <div>
                                <a  href={`https://en.wikipedia.org/wiki/${placeInfo.placeNames.replace(/\s+/g, '_')}`} target="_blank">
                                    see more
                                </a>
                            </div>

                        </div>

                    </InfoBox>}
                </Marker>
            );
        })}
        {/*<DirectionsRenderer directions={props.directions}/>*/}
    </GoogleMap>
));

// Define the helper functions needed by the pending list and selected list
// Define the style of draggable list
const grid = 8;
const getListStyle = isDraggingOver => ({
    background: isDraggingOver ? "lightblue" : "grey",
    padding: grid,
    width: 250
});

const getItemStyle = (isDragging, draggableStyle) => ({
    // some basic styles to make the items look a bit nicer
    userSelect: "none",
    padding: grid * 2,
    margin: `0 0 ${grid}px 0`,

    // change background colour if dragging
    background: isDragging ? "lightgreen" : "lightgrey",

    // styles we need to apply on draggables
    ...draggableStyle
});

// TODO... optimization (choice of array of obj or obj of array)
const reorder = (list, startIndex, endIndex) => {
    const result = Array.from(list);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);

    return result;
};

/**
 * Moves an item from one list to another list.
 */

// TODO... optimization
const move = (source, destination, droppableSource, droppableDestination) => {
    const sourceClone = Array.from(source);
    // console.log(destination);
    const destClone = Array.from(destination);
    const [removed] = sourceClone.splice(droppableSource.index, 1);

    destClone.splice(droppableDestination.index, 0, removed);

    const result = {};
    result[droppableSource.droppableId] = sourceClone;
    result[droppableDestination.droppableId] = destClone;

    return result;
};

export class Map extends React.Component {
    constructor(props) {
        super(props);
        this.handleRemove = this.handleRemove.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.optimizeRouteOrder = this.optimizeRouteOrder.bind(this);
        this.getOptimalRouteOrder = this.getOptimalRouteOrder.bind(this);
        this.onDragEnd = this.onDragEnd.bind(this);
        this.setNewOrder = this.setNewOrder.bind(this);
        this.state = {
            selectedPlaces: {
                place_ids: [],
                place_names: [],
                place_geos: []
            },
            pendingPlaces: {
                place_ids: [],
                place_names: [],
                place_geos: []
            },
            isLoading: true
        };
    }

    componentDidMount() {
        fetch(`${API_ROOT}/fetchPlaces?city=${this.props.city}`)
            .then(response => {
                return response.json();
            })
            .then(data => {
                let place_ids = [], place_names = [], place_geos = [];
                for (let i = 0; i < data.length; i++) {
                    place_ids.push(data[i].place_id);
                    place_names.push(data[i].name);
                    place_geos.push({
                        lat: data[i].lat,
                        lng: data[i].lon
                    })
                }
                this.fetchedPlaces = {
                    place_ids,
                    place_names,
                    place_geos
                };
                this.setState({isLoading: false});
                this.props.handleLoading();
            })
            .catch(e => {
                console.log(e);
            });
    }

    componentDidUpdate(prevProps) {
        if (prevProps.places !== this.props.places) {

            // console.log(this.fetchedPlaces);
            let place_ids = [], place_names = [], place_geos = [];
            let count = this.props.places.place_ids === undefined ? 5 : 0;
            // de-dup

            const selectedPlaces = new Set(this.props.places.place_ids);

            for (let i = 0; i < this.fetchedPlaces.place_ids.length && count < 5; i++) {
                if (!selectedPlaces.has(this.fetchedPlaces.place_ids[i])) {
                    place_ids.push(this.fetchedPlaces.place_ids[i]);
                    place_names.push(this.fetchedPlaces.place_names[i]);
                    place_geos.push(this.fetchedPlaces.place_geos[i]);
                    count++;
                }
            }
            this.setState({
                selectedPlaces: this.props.places,
                pendingPlaces: {
                    place_ids,
                    place_names,
                    place_geos
                }
            });
        }
    }

    // handleRemove

    handleRemove = () => {
    };

    // handleSave

    handleSave = () => {
        this.props.handleSavePlan(
            this.state.selectedPlaces.place_ids,
            this.state.selectedPlaces.place_names,
            this.state.selectedPlaces.place_geos
        );
    };

    // optimize the route

    optimizeRouteOrder = () => {
        console.log("optimize");
        const place_geos = this.state.selectedPlaces.place_geos;
        if (
            place_geos === null ||
            place_geos === undefined ||
            place_geos.length < 3
        ) {
            return;
        }
        // console.log(this.state.selectedGeoInfos);
        // console.log(this.state.selectedAddrs);
        let minLat = place_geos[0].lat,
            minLon = place_geos[0].lng;
        let maxLat = minLat,
            maxLon = minLon;

        for (let i = 1; i < place_geos.length; i++) {
            minLat = Math.min(minLat, place_geos[i].lat);
            minLon = Math.min(minLon, place_geos[i].lng);
            maxLat = Math.max(maxLat, place_geos[i].lat);
            maxLon = Math.max(maxLon, place_geos[i].lng);
        }
        // console.log(minLat, maxLon, maxLat, minLon);
        this.getOptimalRouteOrder(minLat, minLon, maxLat, maxLon);
    };
    getOptimalRouteOrder = (minLat, minLon, maxLat, maxLon) => {
        let directionService = new google.maps.DirectionsService();

        let wayPoints = this.state.selectedPlaces.place_ids.map(placeId => {
            return {
                location: {placeId},
                stopover: true
            };
        });

        // let optimalOrder = [];
        const self = this;
        directionService.route(
            {
                origin: new google.maps.LatLng(minLat, minLon),
                waypoints: wayPoints,
                destination: new google.maps.LatLng(maxLat, maxLon),
                travelMode: google.maps.TravelMode.DRIVING,
                optimizeWaypoints: true
            },
            (result, status) => {
                if (status === google.maps.DirectionsStatus.OK) {
                    // optimalOrder = result.routes[0].waypoint_order;
                    self.setNewOrder(result.routes[0].waypoint_order);
                } else {
                    console.error(`error fetching directions ${status}`);
                }
            }
        );
    };
    setNewOrder = order => {
        //console.log(order);
        let place_ids = [],
            place_names = [],
            place_geos = [];
        for (let i = 0; i < this.state.selectedPlaces.place_ids.length; i++) {
            place_ids.push("");
            place_names.push("");
            place_geos.push("");
        }
        for (let i = 0; i < order.length; i++) {
            place_ids[i] = this.state.selectedPlaces.place_ids[order[i]];
            place_names[i] = this.state.selectedPlaces.place_names[order[i]];
            place_geos[i] = this.state.selectedPlaces.place_geos[order[i]];
        }
        this.setState({
            selectedPlaces: {
                place_ids,
                place_names,
                place_geos
            }
        });
    };

    // function to handle draggable lists
    getList = id => {
        return this.state[`${id}Places`];
    };
    onDragEnd = result => {
        //console.log(result);
        const {source, destination} = result;

        // dropped outside the list
        if (!destination) {
            return;
        }

        if (source.droppableId === destination.droppableId) {
            // console.log(this.state.selectedAddrs);
            const places = this.getList(source.droppableId);
            const place_ids = reorder(
                places.place_ids,
                source.index,
                destination.index
            );

            const place_names = reorder(
                places.place_names,
                source.index,
                destination.index
            );
            const place_geos = reorder(
                places.place_geos,
                source.index,
                destination.index
            );

            let state = {};
            if (source.droppableId === "selected") {
                state = {
                    selectedPlaces: {
                        place_ids,
                        place_names,
                        place_geos
                    }
                };
            } else {
                state = {
                    pendingPlaces: {
                        place_ids,
                        place_names,
                        place_geos
                    }
                };
            }
            // console.log(state);
            this.setState(state);
        } else {
            const sourceList = this.getList(source.droppableId);
            const destList = this.getList(destination.droppableId);
            // console.log(destList);
            // console.log(sourceList);
            console.log({
                user_id: localStorage.getItem('user_id'),
                place_name: sourceList.place_names[source.index],
                event: source.droppableId === 'selected' ? 'dislike' : 'like'
            });
            const place_ids = move(
                sourceList.place_ids,
                destList.place_ids,
                source,
                destination
            );
            const place_names = move(
                sourceList.place_names,
                destList.place_names,
                source,
                destination
            );
            const place_geos = move(
                sourceList.place_geos,
                destList.place_geos,
                source,
                destination
            );

            this.setState({
                pendingPlaces: {
                    place_ids: place_ids.pending,
                    place_names: place_names.pending,
                    place_geos: place_geos.pending
                },
                selectedPlaces: {
                    place_ids: place_ids.selected,
                    place_names: place_names.selected,
                    place_geos: place_geos.selected
                }
            });
        }
    };

    render() {
        return (
            <div>
                {
                    this.state.isLoading ? <Loading text={'Fetching pending places...'}/> :
                        <div className="map-list">
                            <DragDropContext
                                onDragEnd={this.onDragEnd}
                                className="selected-pending-lists"
                            >
                                <div>
                                    <p>Pending places</p>
                                    <div className="drop-scroll">
                                        <Droppable droppableId="pending" >
                                            {(provided, snapshot) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    style={getListStyle(snapshot.isDraggingOver)}
                                                >
                                                    {this.state.pendingPlaces.place_names === undefined ? (
                                                        <p>No places pending</p>
                                                    ) : (
                                                        this.state.pendingPlaces.place_names.map((item, index) => (
                                                            <div key={index}>
                                                                <Draggable
                                                                    key={`pending-${index}`}
                                                                    draggableId={`pending-${index}`}
                                                                    index={index}
                                                                >
                                                                    {(provided, snapshot) => (
                                                                        <div
                                                                            ref={provided.innerRef}
                                                                            {...provided.draggableProps}
                                                                            {...provided.dragHandleProps}
                                                                            style={getItemStyle(
                                                                                snapshot.isDragging,
                                                                                provided.draggableProps.style
                                                                            )}
                                                                            className="selected-places-list-content"
                                                                        >
                                                                            {item}
                                                                            {/*<Icon type="close-circle" theme="filled" onClick={() => {*/}
                                                                            {/*this.handleRemove(index);*/}
                                                                            {/*}}/>*/}
                                                                        </div>
                                                                    )}
                                                                </Draggable>
                                                            </div>
                                                        ))
                                                    )}
                                                    {provided.placeholder}
                                                </div>
                                            )}
                                        </Droppable>
                                    </div>

                                </div>
                                <div className="list">
                                    <p>Selected places</p>
                                    <div className="drop-scroll">
                                        <Droppable droppableId="selected">
                                            {(provided, snapshot) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    style={getListStyle(snapshot.isDraggingOver)}
                                                >
                                                    {this.state.selectedPlaces.place_names === undefined ? (
                                                        <p>No places selected</p>
                                                    ) : (
                                                        this.state.selectedPlaces.place_names.map((item, index) => (
                                                            <div key={index}>
                                                                <Draggable
                                                                    key={`selected-${index}`}
                                                                    draggableId={`selected-${index}`}
                                                                    index={index}
                                                                >
                                                                    {(provided, snapshot) => (
                                                                        <div
                                                                            ref={provided.innerRef}
                                                                            {...provided.draggableProps}
                                                                            {...provided.dragHandleProps}
                                                                            style={getItemStyle(
                                                                                snapshot.isDragging,
                                                                                provided.draggableProps.style
                                                                            )}
                                                                            className="selected-places-list-content"
                                                                        >
                                                                            {item}
                                                                            {/*<Icon type="close-circle" theme="filled" onClick={() => {*/}
                                                                            {/*this.handleRemove(index);*/}
                                                                            {/*}}/>*/}
                                                                        </div>
                                                                    )}
                                                                </Draggable>
                                                            </div>
                                                        ))
                                                    )}
                                                    {provided.placeholder}
                                                </div>
                                            )}
                                        </Droppable>
                                    </div>

                                    <button onClick={this.optimizeRouteOrder}>optimize</button>
                                    <button onClick={this.handleSave}>Save</button>
                                </div>
                            </DragDropContext>

                            <MapWithADirectionsRenderer
                                placeIds={this.state.selectedPlaces.place_ids}
                                placeNames={this.state.selectedPlaces.place_names}
                            />
                        </div>
                }
            </div>
        );
    }
}
