import React, {Component} from "react";
import '../index.css';


export class Plan extends Component {
    render() {
        return (
            <button
                data-hover="click to add!"
                className='plan'
                onMouseEnter={() => {
                    this.props.handleMouseHover(this.props.index);
                }}
                onMouseLeave={() => {
                    this.props.handleMouseHover(-1);
                }}
                onClick={() => {
                    this.props.handleClick(this.props.index);
                }}
            >
                <div>Plan {this.props.index + 1}</div>
                {/*{this.props.place_ids.placeIds.map((place_id, ind) => {*/}
                {/*return <ul key={ind}>*/}
                {/*{place_id}*/}
                {/*</ul>;*/}
                {/*})}*/}
            </button>
        );
    }
}
