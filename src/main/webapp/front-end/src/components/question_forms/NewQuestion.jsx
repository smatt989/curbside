import React from 'react';
import {
  Button,
  ListGroup,
  ListGroupItem,
  Grid,
  Form,
  FormGroup,
  ControlLabel,
  FormControl,
  Col
} from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { saveQuestion, saveQuestionSuccess, saveQuestionError, getQuestion, getQuestionSuccess, getQuestionError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';

class NewQuestion extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      title: '',
      body: '',
      successfulSave: false
    };

    this.changeTitle = this.changeTitle.bind(this)
    this.changeBody = this.changeBody.bind(this)
    this.getQuestionId = this.getQuestionId.bind(this)
    this.readQuestionFromState = this.readQuestionFromState.bind(this)
  }

  changeTitle(e) {
    this.setState({title: e.target.value})
  }

  changeBody(e) {
    this.setState({body: e.target.value})
  }

  getQuestionId() {
    return this.props.match.params.id
  }

  readQuestionFromState() {
    const question = this.props.question.getIn(['question', 'question'])
    this.setState({title: question.get('title'), body: question.get('text'), id: question.get('id')})
  }

  componentDidMount() {
    const questionId = this.getQuestionId()

    if(questionId){
        this.props.getQuestion(this.readQuestionFromState)(questionId)
    }
  }

  render() {

    var disabled = this.state.title.length < 5

    var saveText = this.state.id ? "Save Changes" : "Submit Question"
    var headerText = this.state.id ? "Edit Question" : "New Question"

    if(this.state.successfulSave){
        console.log()
        return <Redirect to={'/question/'+this.props.savedQuestion.getIn(['question', 'id'])} />
    }

    return (
      <div >
        <NavBar inverse={false} />
        <Grid>
            <h3>{headerText}:</h3>
            <div>
                <Form horizontal className="col-md-8 col-md-push-2">
                  <FormGroup>

                    <Col componentClass={ControlLabel} sm={1}>
                      <b>Title:</b>
                    </Col>
                    <Col sm={11}>
                      <FormControl onChange={this.changeTitle} type="text" value={this.state.title} placeholder="What's your medical question?" />
                    </Col>


                  </FormGroup>

                  <FormGroup>
                    <FormControl onChange={this.changeBody} value={this.state.body} componentClass="textarea" placeholder="More details..." />
                  </FormGroup>

                  <FormGroup className="text-xs-center">
                    <Button onClick={() => this.props.saveQuestion(() => this.setState({successfulSave: true}))(this.state.title, this.state.body, this.state.id)} bsStyle="primary" disabled={disabled}>{saveText}</Button>
                  </FormGroup>

                </Form>
            </div>
        </Grid>
      </div>

    );
  }
}

const mapStateToProps = state => {
  return {
    savedQuestion: state.get('saveQuestion'),
    question: state.get('getQuestion')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  const getQuestionWithCallback = (callback) => dispatchPattern(getQuestion, getQuestionSuccess, getQuestionError, callback)
  const saveQuestionWithCallback = (callback) => dispatchPattern(saveQuestion, saveQuestionSuccess, saveQuestionError, callback)

  return {
    saveQuestion: saveQuestionWithCallback,
    getQuestion: getQuestionWithCallback
  };
};

const NewQuestionContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(NewQuestion);

export default NewQuestionContainer;
