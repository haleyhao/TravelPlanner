import React from 'react';
import { Icon, Input, AutoComplete } from 'antd';
import '../index.css';

const Option = AutoComplete.Option;
const OptGroup = AutoComplete.OptGroup;

const dataSource = [{
    title: 'Popular destinations',
    children: [{
        title: 'New York City',
        count: 18000,
    }, {
        title: 'Beijing',
        count: 10600,
    },{
        title: 'Tokyo',
        count: 10020,
    }],
}];

function renderTitle(title) {
    return (
        <span>
      {title}
            <a
                style={{ float: 'right' }}
                href="https://www.google.com/search?q=antd"
                target="_blank"
                rel="noopener noreferrer"
            >More
      </a>
    </span>
    );
}

const options = dataSource.map(group => (
    <OptGroup
        key={group.title}
        label={renderTitle(group.title)}
    >
        {group.children.map(opt => (
            <Option key={opt.title} value={opt.title}>
                {opt.title}
                <span className="certain-search-item-count">{opt.count} people searched</span>
            </Option>
        ))}
    </OptGroup>
)).concat([
    <Option disabled key="all" className="show-all">
        <a
            href="https://www.google.com/search?q=antd"
            target="_blank"
            rel="noopener noreferrer"
        >
            See all results
        </a>
    </Option>,
]);

function Complete() {
    return (
        <div className="certain-category-search-wrapper home-form" style={{ width: 450 }}>
            <AutoComplete
                className="certain-category-search"
                dropdownClassName="certain-category-search-dropdown"
                dropdownMatchSelectWidth={false}
                dropdownStyle={{ width: 500 }}
                size="large"
                style={{ width: '100%' }}
                dataSource={options}
                placeholder="Where do you want to go?"
                optionLabelProp="value"
            >
                <Input suffix={<Icon type="search" className="certain-category-icon" />} />
            </AutoComplete>
        </div>
    );
}

export class Home extends React.Component{
    render(){
        return(
            <div>
                <Complete />
            </div>
        );
    }

}