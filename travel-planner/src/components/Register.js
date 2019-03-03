import React from "react";
import { Form, Input, Button, message } from "antd";
import { API_ROOT } from "../constants";
import { Link } from "react-router-dom";

class RegistrationForm extends React.Component {
  state = {
    confirmDirty: false,
    autoCompleteResult: []
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        console.log("Received values of form: ", values);
        // send request
        fetch(`${API_ROOT}/register`, {
          method: "POST",
          body: JSON.stringify({
            user_id: values.email,
            name: values.name,
            password: values.password
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
              message.success("Registration Succeed!");
              this.props.history.push("/login");
            })
            .catch(e => {
              console.log(e);
              message.error("Registration Failed.");
            });
      }
    });
  };

  handleConfirmBlur = e => {
    const value = e.target.value;
    this.setState({ confirmDirty: this.state.confirmDirty || !!value });
  };

  compareToFirstPassword = (rule, value, callback) => {
    const form = this.props.form;
    if (value && value !== form.getFieldValue("password")) {
      callback("Two passwords that you enter is inconsistent!");
    } else {
      callback();
    }
  };

  validateToNextPassword = (rule, value, callback) => {
    const form = this.props.form;
    if (value && this.state.confirmDirty) {
      form.validateFields(["confirm"], { force: true });
    }
    callback();
  };

  render() {
    const { getFieldDecorator } = this.props.form;

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      }
    };
    const tailFormItemLayout = {
      wrapperCol: {
        xs: {
          span: 24,
          offset: 0
        },
        sm: {
          span: 16,
          offset: 8
        }
      }
    };

    return (
        <Form onSubmit={this.handleSubmit} className="register">
          <Form.Item {...formItemLayout} label="Email">
            {getFieldDecorator("email", {
              rules: [{ required: true, message: "Please input your email!" }]
            })(<Input />)}
          </Form.Item>
          <Form.Item {...formItemLayout} label="Name">
            {getFieldDecorator("name", {
              rules: [{ required: true, message: "Please input your name!" }]
            })(<Input />)}
          </Form.Item>
          <Form.Item {...formItemLayout} label="Password">
            {getFieldDecorator("password", {
              rules: [
                {
                  required: true,
                  message: "Please input your password!"
                },
                {
                  validator: this.validateToNextPassword
                }
              ]
            })(<Input type="password" />)}
          </Form.Item>
          <Form.Item {...formItemLayout} label="Confirm Password">
            {getFieldDecorator("confirm", {
              rules: [
                {
                  required: true,
                  message: "Please confirm your password!"
                },
                {
                  validator: this.compareToFirstPassword
                }
              ]
            })(<Input type="password" onBlur={this.handleConfirmBlur} />)}
          </Form.Item>
          <Form.Item {...tailFormItemLayout}>
            <Button style={{backgroundColor:'#005a62'}} htmlType="submit" >
              <h1 style={{fontSize: 17, color:'#F6F6F6',fontWeight:400}}>Register</h1>
            </Button>
            <p style={{fontSize: 15, color:'#00353d',fontWeight:500}}>
              I already have an account, go back to <Link style={{fontSize: 18, color:'#00474f', fontWeight:500}} to="/login">login</Link>
            </p>
          </Form.Item>
        </Form>
    );
  }
}

export const Register = Form.create({ name: "register" })(RegistrationForm);

