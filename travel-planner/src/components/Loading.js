import React from 'react';
import Spinner from 'react-spinner-material';


export class Loading extends React.Component {


  render() {
    return (
        <div>
          <p>{this.props.text}</p>
          <Spinner size={120} spinnerColor={"#333"} spinnerWidth={2} visible={true} />
        </div>
    );
  }
}