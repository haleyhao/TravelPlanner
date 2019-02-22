import React, {Component}from "react";
import { Input, List, Icon } from "antd";


export class History extends Component {
  state = {
    plans: [{
      index: 0,
      content:"plannnnnnnn 1"

    },{
      index: 1,
      content:"plannnnnnnn 2"
    },{
      index: 2,
      content:"plannnnnnnn 3"
    }]
  };

  handlePressSave = e => {
    const plan = {
      index: this.state.plans.length,
      content: e.target.value,
    };

    const newTodos = this.state.plans.concat(plan);

    this.setState({
      plans: newTodos
    });

    e.target.value = "";
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

  render() {
    return (
        <div className="container">
          <h1>History Plans</h1>
          {/*<Input*/}
              {/*onPressEnter={this.handlePressSave}*/}
          {/*/>*/}

          <List
              locale={{ emptyText: "No History Plan" }}
              dataSource={this.state.plans}
              renderItem={item => (
                  <HistoryItem
                      plan={item}
                      removePlan={this.removePlan}
                  />
              )}
          />
        </div>
    );
  }
}

class HistoryItem extends Component {
  remove = () => {
    this.props.removePlan(this.props.plan.index);
  };

  render() {
    return (
        <List.Item
            actions={[
              <Icon type="close-circle" theme="filled" onClick={this.remove} />
            ]}
        >
          {this.props.plan.content}
        </List.Item>
    );
  }
}
