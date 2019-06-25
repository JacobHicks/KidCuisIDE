import {Link} from 'react-router-dom'
import 'antd/dist/antd.css';
import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/darcula.css';
import {Button, Icon, Layout, Menu} from "antd";
import 'react-reflex/styles.css'
import {ReflexContainer, ReflexSplitter, ReflexElement} from 'react-reflex'
import "./Code.css";
import "./Scrollbar.css";

require("codemirror/mode/clike/clike");
require("codemirror/addon/fold/foldcode");
require("codemirror/addon/fold/xml-fold");
require("codemirror/addon/edit/matchbrackets");
require("codemirror/addon/edit/matchtags");
require("codemirror/addon/edit/closetag");
require("codemirror/addon/edit/closebrackets");
require("codemirror/addon/scroll/simplescrollbars");
require("codemirror/addon/comment/continuecomment");

const React = require('react');
const CodeArea = require('react-codemirror');
const {Header, Content} = Layout;

const codeWindowOptions = {
    scrollbarStyle: "overlay",
    autoFocus: true,
    lineNumbers: true,
    foldGutter: true,
    autoCloseBrackets: true,
    autoCloseTags: true,
    continueComments: true,
    matchTags: true,                //Only works with markup languages
    matchBrackets: true,
    indentUnit: 4,
    undoDepth: 1000,
    viewportMargin: 30,
    theme: "darcula",
    mode: "text/x-java"
};

const consoleWindowOptions = {
    scrollbarStyle: "overlay",
    lineNumbers: false,
    indentUnit: 4,
    undoDepth: 0,
    readOnly: true,
    theme: "darcula",
    mode: "text/x-java"
};

export default class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            code: "", //initial code
            console: "",
            language: "java",
            isRunning: false
        };
    }

    updateCode = (newCode) => {
        this.setState({
            code: newCode
        })
    };

    componentDidMount() {

    }

    render() {
        return (
            <Layout style={{overflow: "hidden"}}>
                <Header style={{height: '5vh'}}>
                    <Menu
                        theme="dark"
                        mode="horizontal"
                        style={{lineHeight: '5vh'}}
                    >
                        <Menu.Item key="1">Run</Menu.Item>
                        <Menu.Item key="2">Save</Menu.Item>
                        <Menu.Item key="3">New</Menu.Item>
                        <Menu.Item key="4">Upload</Menu.Item>
                        <Menu.Item key="5">Theme</Menu.Item>
                        <Menu.Item key="6">Sign out</Menu.Item>
                    </Menu>
                </Header>
                <Content style={{height: "95vh"}}>
                    <ReflexContainer orientation="horizontal">
                        <ReflexElement>
                            <ReflexContainer orientation="vertical">
                                <ReflexElement>

                                </ReflexElement>
                                <ReflexSplitter propagate={true} className="horizontalSplitter"/>
                                <ReflexElement>
                                    <CodeArea className="CodeMirror" value={this.state.code} onChange={this.updateCode}
                                              options={codeWindowOptions}/>
                                </ReflexElement>
                            </ReflexContainer>
                        </ReflexElement>
                        <ReflexSplitter className="verticalSplitter"/>
                        <ReflexElement>
                            <div className="consoleHeader"/>
                            <div className="consoleWindow">
                                <div style={{justifyContent: "left"}}>
                                    <div className="runBar">
                                        <Button className="runButton"
                                                onClick={this.runCode}
                                                disabled={this.state.isRunning}
                                                ghost
                                        >
                                            <Icon className="runIcon" type="caret-right" style={{color: this.state.isRunning ? "#595959" : "#499c54"}}/>
                                        </Button>

                                        <Button className="stopButton"
                                                onClick={this.stopCode}
                                                disabled={!this.state.isRunning}
                                                ghost
                                        >
                                            <div className="stopIcon" style={{backgroundColor: this.state.isRunning ? "#c75450" : "#595959"}}/>
                                        </Button>
                                    </div>
                                </div>

                                <CodeArea className="CodeMirror" value={this.state.console} onChange={this.updateCode}
                                          options={consoleWindowOptions}/>

                                <div className="consoleRightBar"/>
                            </div>
                            <div className="consoleFooter"/>
                        </ReflexElement>
                    </ReflexContainer>
                </Content>
            </Layout>
        )
    }

    runCode = () => {
        console.log("Click");
        this.toggleRunning()
    };

    stopCode = () => {
        console.log("Click");
        this.toggleRunning()
    };

    toggleRunning() {
        this.setState({
            isRunning: !this.state.isRunning
        });
    }
}