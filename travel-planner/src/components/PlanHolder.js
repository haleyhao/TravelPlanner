import React from "react";
import {PLACE_IDS} from "../constants";
import {Plan} from "./Plan";
import {PlacesList} from "./PlacesList";
import Map from "./Map";
import '../index.css';

export class PlanHolder extends React.Component {
  place_ids_arr = PLACE_IDS;

  constructor(props) {
    super(props);
    this.handleMouseHover = this.handleMouseHover.bind(this);
    this.state = {
      isHovering: false,
      placeIds: []
    };
  }

  handleMouseHover(index) {
    if (index < 0) {
      this.setState((prevState) => {
        return {
          isHovering: !prevState.isHovering,
          placeIds: []
        }
      });
      console.log(index);
    } else {
      this.setState((prevState) => {
        return {
          isHovering: !prevState.isHovering,
          placeIds: PLACE_IDS[index].placeIds
        }
      });
    }
  }


  render() {

    return (
        <div>
          <div className='plan-holder'>
            {this.place_ids_arr.map((place_ids, index) => {
              return <Plan key={index} place_ids={place_ids} index={index} handleMouseHover={this.handleMouseHover}/>
            })}
          </div>
          <div className='map-holder'>
            <PlacesList/>
            <Map placeIds={this.state.placeIds}/>
          </div>
        </div>
    );
  }
}
