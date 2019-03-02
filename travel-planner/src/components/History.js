import React, {Component} from "react";
import {List, Icon, message} from "antd";
import {API_ROOT} from "../constants";

export class History extends Component {
    state = {
        plans: [],
        selectedPlan: -1
    };

    componentDidMount() {
        fetch(`${API_ROOT}/history?user_id=${localStorage.getItem('user_id')}`)
            .then(response => {
                return response.json();
            })
            .then(data => {
                const plans = data.map((plan, index) => {
                    return {
                        content: [
                            plan.place_ids,
                            plan.place_names,
                            plan.place_geos.map(place_geo => {
                                return {
                                    lat: place_geo.lat,
                                    lng: place_geo.lon
                                };
                            }),
                        ],
                        plan_id: plan.plan_id,
                        index: index + 1
                    };
                });
                // console.log(plans);
                this.setState({
                    plans
                });
            })
            .catch(e => {
                console.log(e);
            });
    }

    componentDidUpdate(nextProps) {
        if (this.props.savedPlan !== nextProps.savedPlan) {
            this.handlePressSave();
        }
    }

    handlePressSave = () => {
        if (this.state.plans.length > 0) {
            for (let i = 0; i < this.state.plans.length; i++) {
                if (this.state.plans[i].content[0] === this.props.savedPlan.placeIds) {
                    message.info("You have already saved this plan");
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
            ],
            plan_id: new Date().getTime()
        };

        const newPlans = this.state.plans.concat(plan);
        console.log(this.state.selectedPlan);
        // if (this.state.selectedPlan > -1) {
        //     newPlans.splice(this.state.selectedPlan, 1);
        //     for (let i = this.state.selectedPlan; i < newPlans.length; i++) {
        //         newPlans[i].index -= 1;
        //     }
        //     this.deletePlan(this.state.selectedPlan);
        // }
        this.setState({
            plans: newPlans,
            selectedPlan: newPlans.length
        });

        console.log(plan.plan_id);
        fetch(`${API_ROOT}/history`, {
            method: "POST",
            body: JSON.stringify({
                user_id: `${localStorage.getItem('user_id')}`,
                plan_id: `${localStorage.getItem('user_id')}:${plan.plan_id}`,
                place_ids: plan.content[0]
            })
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                }
                throw new Error(response.statusText);
            })
            .then(data => {
                console.log(data);
                message.success("Save success!");
            })
            .catch(e => {
                console.log(e);
                message.error("Save failed.");
            });

        console.log("Plan Saved");
    };

    // API 8 implementation
    deletePlan = (index) => {
        // Fix the CORS problem
        fetch(`https://cors-anywhere.herokuapp.com/${API_ROOT}/history`, {
            method: "DELETE",
            body: JSON.stringify({
                user_id: `${localStorage.getItem('user_id')}`,
                plan_id: `${this.state.plans[index].plan_id}`
            })
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                }
                throw new Error(response.statusText);
            })
            .then(data => {
                console.log(data);
                message.success("Delete success!");
            })
            .catch(e => {
                console.log(e);
                message.error("Delete failed.");
            });
    };

    removePlan = index => {
        let newPlans = [...this.state.plans];
        newPlans.splice(index, 1);

        for (let i = index; i < newPlans.length; i++) {
            newPlans[i].index -= 1;
        }
        let selected = this.state.selectedPlan;
        if (index === selected) {
            selected = newPlans.length === 0 ? -1 : 0;
            if (selected === 0) {
                this.props.handleUpdateMap(newPlans[selected]);
            }
        }
        this.setState({
            plans: newPlans,
            selectedPlan: selected
        });

        this.deletePlan(index);

    };

    updateMap = index => {
        // console.log(this.state);
        // console.log(index);
        this.props.handleUpdateMap(this.state.plans[index]);
        this.setState({selectedPlan: index});
    };

    render() {
        return (
            <div className="container">
                <h1 style={{fontSize: 20, color:'#006170',fontWeight:600}}>History Plans</h1>

                <List
                    locale={{emptyText: "No History Plan"}}
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
            <div>
                <List.Item
                    onClick={this.updateMap}
                >
                    <h1 style={{fontSize: 14, color:'#00474f',fontWeight:700}}>Plan {this.props.plan.index}</h1>

                </List.Item>
                <Icon type="close-circle" theme="filled" onClick={this.remove}/>
            </div>

        );
    }
}