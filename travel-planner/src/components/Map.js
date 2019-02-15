import React from "react"
import {MAP_API_KEY, MAP_LIBRARIES} from "../constants";

const {compose, withProps, lifecycle} = require("recompose");
const {
  withScriptjs,
  withGoogleMap,
  GoogleMap,
  DirectionsRenderer,
} = require("react-google-maps");
/*global google*/

const MapWithADirectionsRenderer = compose(
    withProps({
      googleMapURL: `https://maps.googleapis.com/maps/api/js?key=${MAP_API_KEY}&v=3.exp&libraries=${MAP_LIBRARIES}`,
      loadingElement: <div style={{height: `100%`}}/>,
      containerElement: <div style={{height: `400px`, width: `400px`}}/>,
      mapElement: <div style={{height: `75%`}}/>,
    }),
    withScriptjs,
    withGoogleMap,
    lifecycle({
      componentDidUpdate() {
        if (this.props.placeIds === undefined || this.props.placeIds === null || this.props.placeIds.length < 2) {
          return false;
        }
        const DirectionsService = new google.maps.DirectionsService();
        DirectionsService.route({
          origin: {placeId: this.props.placeIds[0]},
          destination: {placeId: this.props.placeIds[1]},
          travelMode: google.maps.TravelMode.DRIVING,
        }, (result, status) => {
          if (status === google.maps.DirectionsStatus.OK) {
            this.setState({
              directions: result,
            });
          } else {
            console.log(this.props.placeIds);
            console.error(`error fetching directions ${status}`);
          }
        });
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


  render() {
    return (
        <MapWithADirectionsRenderer
            placeIds={this.props.placeIds}
        />
    )
  }
}

export default Map;