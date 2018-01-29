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

    this.state = {text: '', successfulSave: false}

    this.changeText = this.changeText.bind(this)
    this.clearText = this.clearText.bind(this)
  }

  changeText(e) {
    this.setState({text: e.target.value})
  }

  clearText() {
    this.setState({successfulSave: true})
  }

  componentDidMount() {
    if(this.props.answer) {
        this.setState({text: this.props.answer.getIn(['answer', 'text'])})
    }
  }

  render() {

    const questionId = this.props.questionId

    if(this.state.successfulSave){
        console.log('hmm');
        return <Redirect to={'/question/'+questionId} />
    }

    const text = this.state.text
    const disabled = text.length < 5


    const answerId = this.props.answer ? this.props.answer.getIn(['answer', 'id']) : null

    var saveText = answerId ? "Save Changes" : "Submit"

    return (
      <div>
        <h3>Your Answer:</h3>
        <Form horizontal>
            <FormGroup>
                <FormControl onChange={this.changeText} value={text} componentClass="textarea" placeholder="Start your answer..." />
            </FormGroup>
            <Button disabled={disabled} onClick={() => this.props.saveAnswer(this.clearText)(text, questionId, answerId)} bsStyle="primary" className="col-md-2">{saveText}</Button>
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

  const saveAnswerWithCallback = (callback) => dispatchPattern(saveAnswer, saveAnswerSuccess, saveAnswerError, function(){
    ownProps.refresh()
    callback()
  })

  return {
    saveAnswer: saveAnswerWithCallback
  };
};

const NewAnswerContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(NewAnswer);

export default NewAnswerContainer;
