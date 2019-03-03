import React, { Component } from "react";
import { TopBar } from "./TopBar";
import { Main } from "./Main";

class App extends Component {
    state = {
        isLoggedIn: false
    };

    handleSuccessfulLogin = data => {
        localStorage.setItem("user_id", data.user_id);
        this.setState({ isLoggedIn: true });
    };

    handleLogout = () => {
        localStorage.removeItem("user_id");
        this.setState({ isLoggedIn: false });
    };

    render() {
        return (
            <div className="App">
                <TopBar
                    handleLogout={this.handleLogout}
                    isLoggedIn={this.state.isLoggedIn}
                />
                <Main
                    handleSuccessfulLogin={this.handleSuccessfulLogin}
                    isLoggedIn={this.state.isLoggedIn}
                />
            </div>
        );
    }

}

export default App;