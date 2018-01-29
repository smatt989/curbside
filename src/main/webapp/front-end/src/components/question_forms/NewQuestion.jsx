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
import { saveQuestion, saveQuestionSuccess, saveQuestionError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';

class NewQuestion extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      title: '',
      body: ''
    };

    this.changeTitle = this.changeTitle.bind(this)
    this.changeBody = this.changeBody.bind(this)
  }

  changeTitle(e) {
    this.setState({title: e.target.value})
  }

  changeBody(e) {
    this.setState({body: e.target.value})
  }

  render() {
    console.log(this.state)

    var disabled = this.state.title.length < 5

    return (
      <div >
        <NavBar inverse={false} />
        <Grid>
            <h3>New Question:</h3>
            <div>
                <Form horizontal className="col-md-8 col-md-push-2">
                  <FormGroup>

                    <Col componentClass={ControlLabel} sm={1}>
                      <b>Title:</b>
                    </Col>
                    <Col sm={11}>
                      <FormControl onChange={this.changeTitle} type="text" value={this.state.title} placeholder="What's your medial question?" />
                    </Col>


                  </FormGroup>

                  <FormGroup>
                    <FormControl onChange={this.changeBody} value={this.state.body} componentClass="textarea" placeholder="More details..." />
                  </FormGroup>

                  <FormGroup className="text-xs-center">
                    <Button onClick={() => this.props.saveQuestion(this.state.title, this.state.body)} bsStyle="primary" disabled={disabled}>Submit Question</Button>
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
    question: state.get('saveQuestion')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    saveQuestion: dispatchPattern(saveQuestion, saveQuestionSuccess, saveQuestionError)
  };
};

const NewQuestionContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(NewQuestion);

export default NewQuestionContainer;
