import React from 'react';
import { Register } from './Register';
import { Login } from './Login';
import { Home } from './Home';
import { Switch, Route, Redirect } from 'react-router-dom';

export class Main extends React.Component {

    // constructor(props) {
    //     super(props);
    //     let router = context.router;
    // }
    getLogin = () => {
        return this.props.isLoggedIn ? <Redirect to="/home"/> : <Login handleSuccessfulLogin={this.props.handleSuccessfulLogin}/>;
    }
    getHome = () => {
        return this.props.isLoggedIn ? <Home/> : <Redirect to="/login"/>;
    }
    render() {
        return (
            <div className="main">
                <Switch>
                    <Route exact path="/" render={this.getLogin}/>
                    <Route path="/login" render={this.getLogin}/>
                    <Route path="/register" component={Register}/>
                    <Route path="/home" render={this.getHome}/>
                    <Route render={this.getLogin}/>
                </Switch>
            </div>
        );
    }
}
