import React from "react";
import { Register } from "./Register";
import { Login } from "./Login";
import Home from "./Home";
import { Switch, Route, Redirect } from "react-router-dom";
import PlanHolder from "./PlanHolder";

export class Main extends React.Component {
    // constructor(props) {
    //     super(props);
    //     let router = context.router;
    // }
    getLogin = () => {
        return this.props.isLoggedIn ? (
            <Redirect to="/home" />
        ) : (
            <Login handleSuccessfulLogin={this.props.handleSuccessfulLogin} />
        );
    };
    getHome = () => {
        return this.props.isLoggedIn ? <Home /> : <Redirect to="/login" />;
    };
    getResult = () => {
        return this.props.isLoggedIn ? <PlanHolder /> : <Redirect to="/login" />;
    };
    render() {
        return (
            <div className="main">
                <Switch>
                    <Route exact path="/" render={this.getLogin} />
                    <Route path="/login" render={this.getLogin} />
                    <Route path="/register" component={Register} />
                    <Route path="/home" render={this.getHome} />
                    <Route path="/result" component={this.getResult} />
                    <Route render={this.getLogin} />
                </Switch>
            </div>
        );
    }
}