import React from "react";
import { Icon, Input, AutoComplete, message } from "antd";
import "../index.css";
import { API_ROOT } from "../constants";
import { withRouter } from "react-router-dom";

class Home extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
    this.state = {
      cities: []
    };
  }

  // TODO.. implement API 4.1, get the data from back end and display
  componentDidMount() {
    fetch(`${API_ROOT}/get?user_id=${localStorage.getItem("user_id")}`, {
      method: "GET"
    })
      .then(response => {
        if (response.ok) {
          // console.log(response.headers.get('Set-Cookie'));
          return response.text();
        }
        throw new Error(response.statusText);
      })
      .then(data => {
        this.setState({ cities: JSON.parse(data.toString()) });
      })
      .catch(e => {
        console.log(e);
        message.error("Login Failed.");
      });
  }

  // TODO.. implement API 4.2, check availability, if not available, pop out
  handleClick = () => {
    // console.log("handleClick");
    // console.log(this.textInput.props.value === "New York");
    if (this.textInput.props.value === "New York") {
      this.props.history.push({
        pathname: "/result",
        search: `?query=${this.textInput.props.value}`,
        state: { city: this.textInput.props.value }
      });
      // this.props.history.push('/result');
    } else {
      message.error("Sorry, this city is not available right now!");
    }
  };

  render() {
    return (
      <div
        className="certain-category-search-wrapper home-form"
        style={{ width: 450 }}
      >
        <AutoComplete
          className="certain-category-search"
          dropdownClassName="certain-category-search-dropdown"
          dropdownMatchSelectWidth={false}
          dropdownStyle={{ width: 500 }}
          size="large"
          style={{ width: "100%" }}
          dataSource={this.state.cities}
          placeholder="Where do you want to go?"
          optionLabelProp="value"
        >
          <Input
            suffix={
              <Icon
                type="search"
                className="certain-category-icon"
                onClick={this.handleClick}
              />
            }
            ref={input => (this.textInput = input)}
          />
        </AutoComplete>
      </div>
    );
  }
}

export default withRouter(Home);
