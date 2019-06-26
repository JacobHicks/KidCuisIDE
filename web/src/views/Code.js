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
const serverIp = "http://127.0.0.1:5000";

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

export default class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            code: "", //initial code
            console: "",
            language: "java",
            filePath: "",
            fileName: "Main",
            isRunning: false,

        };
    }

    updateCode = (newCode) => {
        this.setState({
            code: newCode
        })
    };

    updateConsole = (newConsole) => {
        this.setState({
            console: newConsole
        })
    };

    render() {
        const consoleOption = {
            scrollbarStyle: "overlay",
            lineNumbers: false,
            indentUnit: 0,
            undoDepth: 0,
            readOnly: !this.state.isRunning,
            theme: "darcula",
            mode: "text/x-java"
        };

        if (this.consoleMarkup !== undefined) {
            this.consoleMarkup.props = {
                options: {
                    readOnly: !this.state.isRunning
                }
            };
        }

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
                                            <Icon className="runIcon" type="caret-right"
                                                  style={{color: this.state.isRunning ? "#595959" : "#499c54"}}/>
                                        </Button>

                                        <Button className="stopButton"
                                                onClick={this.stopCode}
                                                disabled={!this.state.isRunning}
                                                ghost
                                        >
                                            <div className="stopIcon"
                                                 style={{backgroundColor: this.state.isRunning ? "#c75450" : "#595959"}}/>
                                        </Button>
                                    </div>
                                </div>

                                <CodeArea className="CodeMirror" ref={c => {
                                    this.consoleMirror = c;
                                    if (c !== null && this.consoleBinded !== true) {
                                        c.getCodeMirror().on("inputRead", this.sendMessage);
                                        c.getCodeMirror().on("keyHandled", this.keyHandled);
                                        this.consoleBinded = true;
                                    }
                                }}
                                          value={this.state.console} onChange={this.updateConsole}
                                          options={consoleOption}/>

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
        this.msgBuffer = "";
        this.updateConsole("");
        this.toggleRunning();
        let codeForm = {
            name: this.state.fileName,
            path: this.state.filePath,
            language: this.state.language,
            code: this.state.code
        };

        fetch(serverIp + "/run",
            {
                method: "post",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(codeForm),
                credentials: 'include'
            }
        )
            .then(response => {
                if (response.status === 200) { //Good upload
                    let update = () =>
                        fetch(serverIp + "/output")
                            .then(response => response.json())
                            .then(data => {
                                if (!data.eof) {
                                    this.setState({
                                        console: this.state.console + data.output + data.error
                                    }, () => {
                                        console.log(this.consoleMirror.getCodeMirror().getLine(this.consoleMirror.getCodeMirror().lastLine()));
                                        this.consoleMirror.getCodeMirror().focus();
                                        this.consoleMirror.getCodeMirror().setCursor(this.consoleMirror.getCodeMirror().lastLine(), this.consoleMirror.getCodeMirror().getLine(this.consoleMirror.getCodeMirror().lastLine()).length);
                                        update()
                                    });
                                } else {
                                    this.toggleRunning();
                                }
                            });
                    update();
                }
            });
    };

    stopCode = () => {
        this.toggleRunning();
    };

    toggleRunning() {
        this.setState({
            isRunning: !this.state.isRunning
        });
    }

    sendMessage = (instance, changeObj) => {
        if(changeObj.text[0] === " " || changeObj.text[0] === "\n" || changeObj.text[0] === "\t") {
            fetch(serverIp + "/send",
                {
                    method: "post",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                    body: this.msgBuffer + changeObj.text,
                    credentials: 'include'
                }
            );
        }
        else {
            this.msgBuffer += changeObj.text;
        }
    };

    keyHandled = (instance, name, event) => {
        if (this.state.isRunning) {
            if (name === "Enter") {
                this.sendMessage(instance, {text: ["\n"]});
            } else if (name === "Tab") {
                this.sendMessage(instance, {text: ["\t"]});
            } else if (name === "Backspace") {
                this.sendMessage(instance, {text: ["\b"]});
            }
        }
    }
}