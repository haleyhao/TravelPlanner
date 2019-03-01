import React from 'react';
import {API_ROOT} from "../constants";
import {Loading} from "./Loading";
import {Plan} from "./Plan";
import {History} from "./History";
import {Map} from "./Map";
import { withRouter } from 'react-router-dom';

class PlanHolder extends React.Component {


  constructor(props) {
    super(props);
    this.handleMouseHover = this.handleMouseHover.bind(this);
    this.handleClick = this.handleClick.bind(this);
    this.state = {
      isClicked: false,
      isLoading: true,
      plans: [{}, {}, {}],
      hoveredIndex: -1
    };
  }

  componentDidMount() {
    // fetch three plans
    let city = this.props.location.state.city;
    fetch(`${API_ROOT}/recommend?city=${city}&keyword=hao`)
        .then((response) => {
          return response.json();
        })
        .then((data) => {
          const plans = data.map((plan, index) => {
            return {
              place_ids: plan.place_ids,
              place_names: plan.place_names,
              place_geos: plan.place_geos.map((place_geo) => {
                return {
                  lat: place_geo.lat,
                  lng: place_geo.lon
                }
              })
            }
          });
          this.setState({
            isLoading: false,
            plans
          });
        })
        .catch((e) => {
          console.log(e);
        });
  }

  handleMouseHover(index) {
    this.setState({hoveredIndex: index});
  }

  handleClick = () => {
    this.setState({isClicked: true});
  };

  render() {
    return (
        <div>
          {
            this.state.isLoading ? <Loading text="Fetching three suggested plans...."/> :
                <div>
                  {
                    !this.state.isClicked &&
                    <div className='plan-holder'>
                      {this.state.plans.map((plan, index) => {
                        return <Plan key={index} place_ids={plan.place_ids} index={index}
                                     handleMouseHover={this.handleMouseHover}
                                     handleClick={this.handleClick}/>
                      })}

                    </div>

                  }
                  <div className="history-map-holder">
                    {/*<History className="history"/>*/}
                    <div className='map-holder'>
                      <Map places={this.state.hoveredIndex > -1 ? this.state.plans[this.state.hoveredIndex] : {}} />
                    </div>
                  </div>
                </div>

          }
        </div>
    );
  }

}

export default withRouter(PlanHolder);