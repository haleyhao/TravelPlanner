import React from "react"
import {MAP_API_KEY, MAP_LIBRARIES} from "../constants";
import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import {Icon} from "antd";

const {compose, withProps, lifecycle} = require("recompose");
const {
  withScriptjs,
  withGoogleMap,
  GoogleMap,
  DirectionsRenderer,
} = require("react-google-maps");


/*global google*/


const reorder = (list, startIndex, endIndex) => {
  const result = Array.from(list);
  const [removed] = result.splice(startIndex, 1);
  result.splice(endIndex, 0, removed);

  return result;
};

/**
 * Moves an item from one list to another list.
 */
const move = (source, destination, droppableSource, droppableDestination) => {
  const sourceClone = Array.from(source);
  const destClone = Array.from(destination);
  const [removed] = sourceClone.splice(droppableSource.index, 1);

  destClone.splice(droppableDestination.index, 0, removed);

  const result = {};
  result[droppableSource.droppableId] = sourceClone;
  result[droppableDestination.droppableId] = destClone;

  return result;
};
const grid = 8;
const getListStyle = isDraggingOver => ({
  background: isDraggingOver ? 'lightblue' : 'grey',
  padding: grid,
  width: 250
});

const getItemStyle = (isDragging, draggableStyle) => ({
  // some basic styles to make the items look a bit nicer
  userSelect: 'none',
  padding: grid * 2,
  margin: `0 0 ${grid}px 0`,

  // change background colour if dragging
  background: isDragging ? 'lightgreen' : 'lightgrey',

  // styles we need to apply on draggables
  ...draggableStyle
});

const MapWithADirectionsRenderer = compose(
    withProps({
      googleMapURL: `https://maps.googleapis.com/maps/api/js?key=${MAP_API_KEY}&v=3.exp&libraries=${MAP_LIBRARIES}`,
      loadingElement: <div style={{height: `100%`}}/>,
      containerElement: <div style={{height: `450px`, width: `450px`}}/>,
      mapElement: <div style={{height: `100%`}}/>,
    }),
    withScriptjs,
    withGoogleMap,
    lifecycle({
      componentDidMount() {
        this.setState({
          directionService: new google.maps.DirectionsService(),
        });
        this.props.handlePlaces();
      },
      componentDidUpdate(prevProps, prevState) {
        // console.log(this.state);

        if (this.props.placeIds === undefined || this.props.placeIds === null || this.props.placeIds.length < 2) {
          if (this.state.directions !== undefined) {
            this.setState({directions: undefined});
          }
          return;

        }
        // if the place ids changed, resend the request to MAP API
        if (prevProps.placeIds !== this.props.placeIds) {

          // generate the way points
          // console.log('a');
          let len = this.props.placeIds.length;
          let wypts = [];
          for (let i = 1; i < len - 1; i++) {
            wypts.push({
              location: {placeId: this.props.placeIds[i]},
              stopover: true
            });
          }
          // console.log(wypts);
          this.state.directionService.route({
            origin: {placeId: this.props.placeIds[0]},
            waypoints: wypts,
            destination: {placeId: this.props.placeIds[len - 1]},
            travelMode: google.maps.TravelMode.DRIVING,
          }, (result, status) => {
            if (status === google.maps.DirectionsStatus.OK) {
              // console.log(prevProps.placeIds);
              // console.log(this.props.placeIds);
              this.setState({
                directions: result,
              });
            } else {
              console.error(`error fetching directions ${status}`);
            }
          });
        }
      },
      componentWillUnmount() {
        this.setState();
      }
    })
)(props =>
    <GoogleMap
        defaultZoom={7}
        defaultCenter={{lat: 41.85, lng: -87.65}}
    >
      {props.directions && <DirectionsRenderer directions={props.directions}/>}
      {/*<DirectionsRenderer directions={props.directions}/>*/}
    </GoogleMap>
);

class Map extends React.PureComponent {

  // constructor(props) {
  //   super(props);
  // }
  // componentDidMount () {
  //   this.mapInstance = map.context[MAP]
  // }

  state = {
    selectedPlaceIds: [],
    selectedAddrs: [],
    selectedGeoInfos: []
  };

  componentDidUpdate(prevProps) {

    if (prevProps.placeIds !== this.props.placeIds) {

      this.setState({
        selectedPlaceIds: this.props.placeIds,
        selectedAddrs: this.props.placeNames,
        selectedGeoInfos: this.props.placeGeos
      });

    }
  }

  handleRemove = (index) => {
    this.setState((prevState) => ({
      selectedPlaceIds: [...prevState.selectedPlaceIds.slice(0, index), ...prevState.selectedPlaceIds.slice(index + 1)],
      selectedAddrs: [...prevState.selectedAddrs.slice(0, index), ...prevState.selectedAddrs.slice(index + 1)],
      selectedGeoInfos: [...prevState.selectedGeoInfos.slice(0, index), ...prevState.selectedGeoInfos.slice(index + 1)],

    }));
  };


  getOptimalRouteOrder = (minLat, minLon, maxLat, maxLon) => {


    let directionService = new google.maps.DirectionsService();

    let wayPoints = this.state.selectedPlaceIds.map((placeId) => {
      return {
        location: {placeId},
        stopover: true
      };
    });

    // let optimalOrder = [];
    const self = this;
    directionService.route({
      origin: new google.maps.LatLng(minLat, minLon),
      waypoints: wayPoints,
      destination: new google.maps.LatLng(maxLat, maxLon),
      travelMode: google.maps.TravelMode.DRIVING,
      optimizeWaypoints: true
    }, (result, status) => {
      if (status === google.maps.DirectionsStatus.OK) {
        // optimalOrder = result.routes[0].waypoint_order;
        self.setNewOrder(result.routes[0].waypoint_order);
      } else {
        console.error(`error fetching directions ${status}`);
      }
    });

  };

  setNewOrder = (order) => {
    console.log(order);
    let selectedPlaceIds = [], selectedAddrs = [], selectedGeoInfos = [];
    for (let i = 0; i < this.state.selectedPlaceIds.length; i++) {
      selectedPlaceIds.push("");
      selectedAddrs.push("");
      selectedGeoInfos.push("");
    }
    for (let i = 0; i < order.length; i++) {
      selectedPlaceIds[i] = this.state.selectedPlaceIds[order[i]];
      selectedAddrs[i] = this.state.selectedAddrs[order[i]];
      selectedGeoInfos[i] = this.state.selectedGeoInfos[order[i]];
    }
    // console.log(selectedAddrs);
    this.setState({
      selectedPlaceIds,
      selectedAddrs,
      selectedGeoInfos
    });
  };

  id2List = {
    droppable: 'items',
    selected: 'selectedAddrs'
  };

  optimizeRouteOrder = () => {

    if (this.state.selectedGeoInfos === null || this.state.selectedGeoInfos === undefined || this.state.selectedGeoInfos.length < 3) {
      return;
    }
    // console.log(this.state.selectedGeoInfos);
    // console.log(this.state.selectedAddrs);
    let minLat = this.state.selectedGeoInfos[0].lat, minLon = this.state.selectedGeoInfos[0].lng;
    let maxLat = minLat, maxLon = minLon;

    for (let i = 1; i < this.state.selectedGeoInfos.length; i++) {
      minLat = Math.min(minLat, this.state.selectedGeoInfos[i].lat);
      minLon = Math.min(minLon, this.state.selectedGeoInfos[i].lng);
      maxLat = Math.max(maxLat, this.state.selectedGeoInfos[i].lat);
      maxLon = Math.max(maxLon, this.state.selectedGeoInfos[i].lng);

    }
    // console.log(minLat, maxLon, maxLat, minLon);
    this.getOptimalRouteOrder(minLat, minLon, maxLat, maxLon);

  };

  getList = id => this.state[this.id2List[id]];

  onDragEnd = result => {
    const {source, destination} = result;

    // dropped outside the list
    if (!destination) {
      return;
    }

    if (source.droppableId === destination.droppableId) {
      // console.log(this.state.selectedAddrs);
      const reorderedAddrs = reorder(
          this.getList(source.droppableId),
          source.index,
          destination.index
      );

      const reorderedPlaceIds = reorder(
          this.state.selectedPlaceIds,
          source.index,
          destination.index
      );
      const reorderedGeoInfos = reorder(
          this.state.selectedGeoInfos,
          source.index,
          destination.index
      );

      let state = {};
      // console.log(reorderedAddrs);

      if (source.droppableId === 'selected') {
        state = {
          selectedAddrs: reorderedAddrs,

          selectedPlaceIds: reorderedPlaceIds,

          selectedGeoInfos: reorderedGeoInfos

        };
      }

      this.setState(state);
    } else {
      const result = move(
          this.getList(source.droppableId),
          this.getList(destination.droppableId),
          source,
          destination
      );

      this.setState({
        items: result.droppable,
        selected: result.droppable2
      });
    }
  };

  render() {
    return (
        <div className="map-list">
          <div className='selected-places-list'>
            <p>Selected places</p>
            <DragDropContext onDragEnd={this.onDragEnd}>
              <Droppable droppableId="selected">
                {(provided, snapshot) => (
                    <div
                        ref={provided.innerRef}
                        style={getListStyle(snapshot.isDraggingOver)}>
                      {this.state.selectedAddrs === undefined ? null :
                          this.state.selectedAddrs.map((item, index) => (
                              <div key={index}>
                                <Draggable
                                    key={`drag-${index}`}
                                    draggableId={`${index}`}
                                    index={index}>
                                  {(provided, snapshot) => (
                                      <div
                                          ref={provided.innerRef}
                                          {...provided.draggableProps}
                                          {...provided.dragHandleProps}
                                          style={getItemStyle(
                                              snapshot.isDragging,
                                              provided.draggableProps.style
                                          )}
                                          className='selected-places-list-content'
                                      >
                                        {item}
                                        <Icon type="close-circle" theme="filled" onClick={() => {
                                          this.handleRemove(index);
                                        }}/>
                                      </div>
                                  )}
                                </Draggable>

                              </div>

                          ))}
                      {provided.placeholder}
                    </div>
                )}
              </Droppable>
            </DragDropContext>


            <button onClick={this.optimizeRouteOrder}>optimize</button>
            <button>Save</button>
          </div>
          <MapWithADirectionsRenderer
              placeIds={this.state.selectedPlaceIds}
              handlePlaces={this.props.handlePlaces}
          />
        </div>
    );
  }
}

export default Map;