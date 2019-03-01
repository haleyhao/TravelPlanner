import React, { Component } from "react";
import { List, Icon } from "antd";

export class History extends Component {
  state = {
    plans: []
  };

  componentDidMount() {
    // TODO: API 7
  }

  componentDidUpdate(nextProps) {
    if (this.props.savedPlan != nextProps.savedPlan) {
      this.handlePressSave();
    }
  }

  handlePressSave = () => {
    if (this.state.plans.length > 0) {
      for (let i = 0; i < this.state.plans.length; i++) {
        if (this.state.plans[i].content[0] === this.props.savedPlan.placeIds) {
          console.log("Same plan.");
          // TODO: API 5
          return;
        }
      }
    }
    const plan = {
      index: this.state.plans.length + 1,
      content: [
        this.props.savedPlan.placeIds,
        this.props.savedPlan.addrs,
        this.props.savedPlan.geoInfos
      ]
    };

    const newPlans = this.state.plans.concat(plan);
    this.setState({
      plans: newPlans
    });

    console.log("Plan Saved");
  };

  removePlan = index => {
    let newPlans = [...this.state.plans];
    newPlans.splice(index, 1);

    for (let i = index; i < newPlans.length; i++) {
      newPlans[i].index -= 1;
    }

    this.setState({
      plans: newPlans
    });
  };

  updateMap = index => {
    this.props.handleUpdateMap(this.state.plans[index]);
  };

  render() {
    return (
      <div className="container">
        <h1>History Plans</h1>

        <List
          locale={{ emptyText: "No History Plan" }}
          dataSource={this.state.plans}
          renderItem={item => (
            <HistoryItem
              plan={item}
              removePlan={this.removePlan}
              updateMap={this.updateMap}
            />
          )}
        />
      </div>
    );
  }
}

class HistoryItem extends Component {
  remove = () => {
    this.props.removePlan(this.props.plan.index - 1);
  };

  updateMap = () => {
    this.props.updateMap(this.props.plan.index - 1);
  };

  render() {
    return (
      <List.Item
        actions={[
          <Icon type="close-circle" theme="filled" onClick={this.remove} />
        ]}
        onClick={this.updateMap}
      >
        Plan {this.props.plan.index}
      </List.Item>
    );
  }
}
