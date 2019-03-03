import React from "react";
import { Form, Icon, Input, Button, message } from "antd";
import { API_ROOT } from "../constants";
import { Link } from "react-router-dom";

class NormalLoginForm extends React.Component {
    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                console.log("Received values of form: ", values);
                fetch(`${API_ROOT}/login`, {
                    method: "POST",
                    body: JSON.stringify({
                        user_id: values.username,
                        password: values.password
                    })
                })
                    .then(response => {
                        if (response.ok) {
                            // console.log(response.headers.get('Set-Cookie'));
                            return response.text();
                        }
                        throw new Error(response.statusText);
                    })
                    .then(data => {
                        message.success("Login Success!");
                        this.props.handleSuccessfulLogin(JSON.parse(data.toString()));
                    })
                    .catch(e => {
                        console.log(e);
                        message.error("Login Failed.");
                    });
            }
        });
    };

    render() {
        const { getFieldDecorator } = this.props.form;
        return (

            <Form onSubmit={this.handleSubmit} className="login-form" >

                <Form.Item>
                    {getFieldDecorator("username", {
                        rules: [{ required: true, message: "Please input your username!" }]
                    })(
                        <Input
                            prefix={<Icon type="user" style={{ color: "rgba(0,0,0,.25)" }} />}
                            placeholder="Username"
                        />
                    )}
                </Form.Item>
                <Form.Item>
                    {getFieldDecorator("password", {
                        rules: [{ required: true, message: "Please input your Password!" }]
                    })(
                        <Input
                            prefix={<Icon type="lock" style={{ color: "rgba(0,0,0,.25)" }} />}
                            type="password"
                            placeholder="Password"
                        />
                    )}
                </Form.Item>
                <Form.Item>
                    <Button
                        type="primary"
                        htmlType="submit"
                        className="login-form-button"
                    >
                        <h1 style={{fontSize: 18, color:'#F6F6F6',fontWeight:500}}> Log in </h1>

                    </Button>
                    <h1 style={{fontSize: 12, color:'#00474f', fontWeight:500}}>Or</h1>
                    <Link to="/register" style={{fontSize: 15, color:'#00474f', fontWeight:500}}>register now!</Link>
                </Form.Item>
            </Form>

        );
    }
}

export const Login = Form.create({ name: "normal_login" })(NormalLoginForm);