import React from "react";
import { API_ROOT } from "../constants";
import { Loading } from "./Loading";
import { Plan } from "./Plan";
import { History } from "./History";
import { Map } from "./Map";
import { withRouter } from "react-router-dom";

class PlanHolder extends React.Component {
  constructor(props) {
    super(props);
    this.handleMouseHover = this.handleMouseHover.bind(this);
    this.handleClick = this.handleClick.bind(this);
    this.handleLoading = this.handleLoading.bind(this);
    this.state = {
      isClicked: false,
      isLoading: true,
      plans: [{}, {}, {}],
      hoveredIndex: -1,
      savedPlan: {},
      fromHistoryList: false
    };
  }

  fetchedPlans = [];

  componentDidMount() {
    // fetch three plans
    let city = this.props.location.state.city;
    fetch(`${API_ROOT}/recommend?city=${city}&keyword=hao`)
      .then(response => {
        return response.json();
      })
      .then(data => {
        const plans = data.map((plan, index) => {
          return {
            place_ids: plan.place_ids,
            place_names: plan.place_names,
            place_geos: plan.place_geos.map(place_geo => {
              return {
                lat: place_geo.lat,
                lng: place_geo.lon
              };
            })
          };
        });
        this.setState({
          // isLoading: false,
          plans,
        });
        this.fetchedPlans = plans;
      })
      .catch(e => {
        console.log(e);
      });
  }

  handleLoading = () => {
    this.setState({isLoading: false});
  };

  handleMouseHover(index) {
    // console.log(this.state.plans);
    // console.log(index);
    // console.log(this.state);
    this.setState({ hoveredIndex: index });
  }

  handleClick = () => {
    // console.log(this.state.fromHistoryList);
    if (this.state.isClicked) {
      this.setState({
        isClicked: false,
        fromHistoryList: false,
        plans: this.fetchedPlans,
      })
    } else {
      this.setState({
        isClicked: true
      })
    }
  };

  handleSavePlan = (selectedPlaceIds, selectedAddrs, selectedGeoInfos) => {
    this.setState({
      savedPlan: {
        placeIds: selectedPlaceIds,
        addrs: selectedAddrs,
        geoInfos: selectedGeoInfos
      }
    });
  };

  handleUpdateMap = plan => {
    this.setState({
      fromHistoryList: true,
      plans: {
        place_ids: plan.content[0],
        place_names: plan.content[1],
        place_geos: plan.content[2]
      }
      // placeIds: plan.content[0],
      // placeNames: plan.content[1],
      // placeGeos: plan.content[2]
    });
  };

  render() {
    return (
      <div>
        {this.state.isLoading ? (
          <Loading text="Fetching three suggested plans...." />
        ) : (
          <div>
            {this.state.isClicked ?
                <button onClick={this.handleClick}>Show all the three plans</button> :
              <div className="plan-holder">
                {this.state.plans.map((plan, index) => {
                  return (
                    <Plan
                      key={index}
                      place_ids={plan.place_ids}
                      index={index}
                      handleMouseHover={this.handleMouseHover}
                      handleClick={this.handleClick}
                    />
                  );
                })}
              </div>
            }
          </div> )}
            <div>
            <div className="history-map-holder">
              {
                this.state.isClicked &&
                <History
                    className="history"
                    savedPlan={this.state.savedPlan}
                    handleUpdateMap={this.handleUpdateMap}
                />
              }

              <div className="map-holder">
                {this.state.fromHistoryList ? (
                  <Map
                    places={this.state.plans}
                    handleSavePlan={this.handleSavePlan}
                    city={this.props.location.state.city}
                    handleLoading={this.handleLoading}
                  />
                ) : (
                  <Map
                    places={
                      this.state.hoveredIndex > -1
                        ? this.state.plans[this.state.hoveredIndex]
                        : {}
                    }
                    handleSavePlan={this.handleSavePlan}
                    city={this.props.location.state.city}
                    handleLoading={this.handleLoading}
                  />
                )}
              </div>
            </div>
          </div>
      </div>
    );
  }
}

export default withRouter(PlanHolder);
