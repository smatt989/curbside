import React from 'react';
import {
  Button,
  ListGroup,
  ListGroupItem,
  FormGroup,
  FormControl,
  Form,
  ControlLabel,
  Col
} from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { saveAnswer, saveAnswerSuccess, saveAnswerError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';

class NewAnswer extends React.Component {
  constructor(props) {
    super(props);

    this.state = {text: ''}

    this.changeText = this.changeText.bind(this)
  }

  changeText(e) {
    this.setState({text: e.target.value})
  }

  render() {

    //console.log(this.questionId)

    const text = this.state.text
    const disabled = text.length < 5
    const questionId = this.props.questionId

    return (
      <div>
        <h3>Your Answer:</h3>
        <Form horizontal>
            <FormGroup>
                <FormControl onChange={this.changeText} value={text} componentClass="textarea" placeholder="Start your answer..." />
            </FormGroup>
            <Button disabled={disabled} onClick={() => this.props.saveAnswer(text, questionId)} bsStyle="primary" className="col-md-2">Submit</Button>
        </Form>
      </div>

    );
  }
}

const mapStateToProps = state => {
  return {
    questionFeed: state.get('getQuestionFeed')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {

  return {
    saveAnswer: dispatchPattern(saveAnswer, saveAnswerSuccess, saveAnswerError, ownProps.refresh)
  };
};

const NewAnswerContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(NewAnswer);

export default NewAnswerContainer;
