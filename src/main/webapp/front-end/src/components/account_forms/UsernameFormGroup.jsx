import React from 'react';
import { connect } from 'react-redux';
import FormGroupBase from '../shared/FormGroupBase.jsx';

class UsernameFormGroup extends React.Component {
  constructor(props) {
    super(props);
    this.regexp = new RegExp(/^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+$/);
    this.validation = (state) => {
      if (state.focused || !state.hasFocused) {
        return null;
      }

      const { value } = this.props.usernameInputProps;
      if (value.length > 0 && this.regexp.exec(value)) {
        return 'success';
      }

      return 'error';
    };

    this.onChange = (e) => this.props.onChange(e.target.value, this.props.usernameInputProps.action);
  }

  render() {
    const { value, placeholder } = this.props.usernameInputProps;
    const baseProps = {
      type: 'text',
      validation: this.validation,
      label: 'Username',
      placeholder: placeholder,
      onChange: this.onChange,
      value: value
    };

    return <FormGroupBase baseProps={baseProps} />;
  }
}

const mapStateToProps = state => { return {}; };
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    onChange: (username, action) => dispatch(action(username))
  };
};

const UsernameFormGroupContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(UsernameFormGroup);

export default UsernameFormGroupContainer;
