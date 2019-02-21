import React from "react";
import {PLACE_IDS} from "../constants";
import {Plan} from "./Plan";
import Map from "./Map";
import '../index.css';
import {geocodeByPlaceId, getLatLng} from "react-places-autocomplete";

export class PlanHolder extends React.Component {
  place_ids_arr = PLACE_IDS;

  constructor(props) {
    super(props);
    this.handleMouseHover = this.handleMouseHover.bind(this);
    this.handleClick = this.handleClick.bind(this);
    this.handlePlaces = this.handlePlaces.bind(this);
    this.saveName = this.saveName.bind(this);
    this.saveGeoInfo = this.saveGeoInfo.bind(this);
    this.state = {
      isHovering: false,
      placeIds: [],
      placeNames: [],
      placeGeos: [],
      isClicked: false,
      hoveredIndex: -1
    };
  }

  async componentDidMount() {
    setTimeout(this.handlePlaces, 500);
  }

  handlePlaces = async () => {

    // console.log(this.state);

    let placeIds = PLACE_IDS.map((PLACE_ID) => {
      return PLACE_ID.placeIds;
    });

    let placeNames = [];
    let placeGeos = [];

    // construct the placeNames and placeGeos

    for (let i = 0; i < placeIds.length; i++) {
      placeNames = [...placeNames, []];
      placeGeos = [...placeGeos, []];
      for (let j = 0; j < placeIds[i].length; j++) {
        placeNames[i].push('');
        placeGeos[i].push('');
      }

    }


    for (let i = 0; i < placeIds.length; i++) {
      for (let j = 0; j < placeIds[i].length; j++) {
        await geocodeByPlaceId(placeIds[i][j])
            .then((results) => {
              // console.log(i, j);
              // console.log(results[0].address_components[0].long_name);
              placeNames[i][j] = results[0].address_components[0].long_name;
              // self.saveName(results[0].address_components[0].long_name, i);
              // console.log(this.placeNames);
              return getLatLng(results[0]);
            })
            .then(({lat, lng}) => {
                  // console.log('Successfully got latitude and longitude', {lat, lng});
                  placeGeos[i][j] = {lat, lng};
                }
            )
            .catch(error => console.error(error));

      }

    }

    this.setState({
      placeNames,
      placeGeos,
      placeIds
    });

    // console.log(this.state.placeNames);

  };

  saveName = (addr, end) => {
    // console.log(this.state.placeNames);
    // console.log(addr);
    // console.log(this.state.placeNames);
    // console.log(this);
    this.placeNames = [...this.placeNames.slice(0, end), [...this.placeNames[end], addr]];
    // console.log(this.placeNames);
  };

  saveGeoInfo = (geoInfo, end) => {
    // console.log(this.state);
    this.setState((prevState) => ({
      geoInfos: [...prevState.geoInfos.slice(0, end), [...prevState.geoInfos[end], geoInfo]]
    }));
    // console.log(this.state.geoInfos);
  };

  handleMouseHover(index) {
    if (this.state.isClicked) {
      return;
    }
    this.setState((prevState) => {
      return {
        isHovering: !prevState.isHovering,
        hoveredIndex: index
      }
    });
  }

  handleClick() {
    console.log('handleCLick');
    this.setState({isClicked: true});
  }


  render() {

    return (
        <div>
          {
            !this.state.isClicked &&
            <div className='plan-holder'>
              {this.place_ids_arr.map((place_ids, index) => {
                return <Plan key={index} place_ids={place_ids} index={index}
                             handleMouseHover={this.handleMouseHover} handleClick={this.handleClick}/>
              })}
            </div>
          }
          <div className='map-holder'>
            <Map placeIds={this.state.hoveredIndex > -1 ? this.state.placeIds[this.state.hoveredIndex] : []}
                 placeNames={this.state.hoveredIndex > -1 ? this.state.placeNames[this.state.hoveredIndex] : []}
                 placeGeos={this.state.hoveredIndex > -1 ? this.state.placeGeos[this.state.hoveredIndex] : []}/>
          </div>
        </div>
    );
  }
}
