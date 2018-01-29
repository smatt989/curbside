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
import { saveComment, saveCommentSuccess, saveCommentError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';

class NewComment extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
        text: ''
    }

    this.changeText = this.changeText.bind(this);
    this.clearText = this.clearText.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  changeText(e) {
    this.setState({text: e.target.value})
  }

  clearText() {
    this.setState({text: ''})
  }

  onSubmit() {
    const text = this.state.text
    this.props.saveComment(this.clearText)(text, this.props.questionId, this.props.answerId)
  }

  render() {

    const text = this.state.text
    const disabled = text.length < 5

    return (
      <div>
        <Form horizontal>
            <FormGroup bsSize="small" className="col-md-10">
                <Col md={2}>
                    <ControlLabel><b>Matt</b></ControlLabel>{' '}
                </Col>
                <Col md={10}>
                    <FormControl onChange={this.changeText} value={text} componentClass="textarea" placeholder="Add a comment..." />
                </Col>
            </FormGroup>
            <Button disabled={disabled} onClick={this.onSubmit} className="col-md-2">Submit</Button>
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

  const saveCommentWithCallback = (callback) => dispatchPattern(saveComment, saveCommentSuccess, saveCommentError, function(){
    ownProps.refresh()
    callback()
  })

  return {
    saveComment: saveCommentWithCallback
  };
};

const NewCommentContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(NewComment);

export default NewCommentContainer;
