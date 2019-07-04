import {Link} from 'react-router-dom';
import 'antd/dist/antd.css';
import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/darcula.css';
import {Button, Dropdown, Icon, Input, Layout, Menu, Modal} from "antd";
import 'react-reflex/styles.css';
import {ReflexContainer, ReflexSplitter, ReflexElement} from 'react-reflex';
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
const {Content} = Layout;
const {SubMenu} = Menu;
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


export default class Code extends React.Component {
    constructor(props) {
        super(props);

        this.fileMenu = (
            <Menu className="subMenu" onClick={(item) => this.handleMenuClick(this.openModal, item)}>
                <SubMenu
                    className="subMenuEntryMenu"
                    key="new"
                    onTitleClick={this.handleFileNewClick}
                    title={
                        <div className="subMenuEntryMenuTitle">
                            <Icon className="menuIcon" type="plus-circle" theme="filled"/>
                            New...
                        </div>
                    }>

                    <Menu.Item key="newFolder" className="subMenuEntry">
                        <div className="subMenuEntryMenuTitle">
                            <Icon className="menuIcon" type="folder-add" theme="filled"/>
                            Folder
                        </div>
                    </Menu.Item>

                    <Menu.Item key="newJava" className="subMenuEntry">
                        <div className="subMenuEntryMenuTitle">
                            <Icon className="menuIcon" type="code" theme="filled"/>
                            Java Source File
                        </div>
                    </Menu.Item>

                    <Menu.Item key="newText" className="subMenuEntry">
                        <div className="subMenuEntryMenuTitle">
                            <Icon className="menuIcon" type="file-text" theme="filled"/>
                            Text file
                        </div>
                    </Menu.Item>

                    <Menu.Item key="newOther" className="subMenuEntry">
                        <div className="subMenuEntryMenuTitle">
                            <Icon className="menuIcon" type="file-unknown" theme="filled"/>
                            Other
                        </div>
                    </Menu.Item>

                </SubMenu>
            </Menu>
        );
        this.state = {
            code: "", //initial code
            console: "",
            language: "java",
            filePath: "",
            fileName: "Main",
            isRunning: false,
            fileStructure: {},
            creatingFile: false,
            newFileType: ""
        };

        this.handleNewNameChange = this.handleNewNameChange.bind(this);
        this.handleNewPathChange = this.handleNewPathChange.bind(this);
        this.createFile = this.createFile.bind(this);
        this.updateFileTree = this.updateFileTree.bind(this);
    };

    componentDidMount() {
        this.updateFileTree();
    }

    updateFileTree() {
        fetch(serverIp + "/tree")
            .then(response => response.json())
            .then(data => {
                this.setState({
                    fileStructure: data
                });
            })
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
                <Modal
                    className="codeModal"
                    title={
                        <div className="codeModalTitle">
                            Create new {this.state.newFileType}
                        </div>
                    }
                    closable={false}
                    visible={this.state.creatingFile}

                    onCancel={() =>
                        this.setState({
                            creatingFile: false
                        })}

                    onOk={() => {
                        this.setState({
                            creatingFile: false
                        });

                        this.createFile();
                    }}

                    okButtonProps={{ghost: true, style: {borderColor: "#4b4b4b", color: "#bbbbbb"}}}
                    cancelButtonProps={{ghost: true, style: {borderColor: "#4b4b4b", color: "#bbbbbb"}}}
                >
                    <form>
                        <div style={{display: "flex"}}>
                            <div className="modalInputDescriptor">
                                Name:
                            </div>
                            <input className="modalInput" type="text" value={this.state.newName}
                                   onChange={this.handleNewNameChange}/>
                        </div>

                        <div style={{display: "flex"}}>
                            <div className="modalInputDescriptor">
                                Path:
                            </div>
                            <input className="modalInput" type="text" value={this.state.newPath}
                                   onChange={this.handleNewPathChange}/>
                        </div>
                    </form>
                </Modal>
                <div className="menuBar">
                    <Dropdown overlay={this.fileMenu} trigger={["click"]} overlayStyle={{
                        background: "#3C3F41",
                        borderRadius: 0,
                        border: "solid",
                        borderWidth: 1,
                        borderColor: "#4b4b4b",
                        paddingTop: 0,
                        top: 0,
                        lineHeight: 1
                    }}>
                        <span className="menuText">File</span>
                    </Dropdown>
                </div>
                <Content className="content">
                    <div className="verticalBarLeft"/>
                    <ReflexContainer orientation="horizontal">
                        <ReflexElement>
                            <ReflexContainer orientation="vertical">
                                <ReflexElement style={{background: "#3C3F41"}}>
                                    <div className="horizontalBarTop">
                                        <div className="treeHeader">
                                            <Icon
                                                type="project"
                                                theme="filled"
                                                style={{fontSize: 14, marginRight: 6, color: "#ADADAD"}}
                                            />
                                            Project Manager
                                        </div>
                                    </div>
                                    <div style={{marginLeft: 8, marginTop: 2}}>
                                        {this.renderTree()}
                                    </div>
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
                                                onClick={this.kill}
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

                                <div className="verticalBar"/>
                            </div>
                            <div className="consoleFooter"/>
                        </ReflexElement>
                    </ReflexContainer>
                    <div className="verticalBarRight"/>
                </Content>
            </Layout>
        )
    }

    renderTree() {
        let res = [];
        for (let i = 0; this.state.fileStructure[i] !== undefined; i++) {
            if (this.state.fileStructure[i].root) {
                res.push(this.renderNode(i, this.state.fileStructure));
            }
        }
        return res;
    }

    renderNode(node) {
        let markup = [];
        if (this.state.fileStructure[node].children !== undefined && this.state.fileStructure[node].children !== null && this.state.fileStructure[node].open) {
            for (let i = 0; i < this.state.fileStructure[node].children.length; i++) {
                markup.push(this.renderNode(this.state.fileStructure[node].children[i]));
            }
        }
        return (
            <div>
                <div className="treeText">
                    {this.renderFold(node)}
                    {this.renderIcon(this.state.fileStructure[node].type)}
                    <span style={{fontStyle: this.state.fileStructure[node].root ? "italic" : ""}}>
                        {this.state.fileStructure[node].name}
                    </span>
                    {this.renderWrite(node)}
                </div>
                <div style={{marginLeft: 16}}>
                    {markup}
                </div>
            </div>
        )
    }

    renderFold(node) {
        if (this.state.fileStructure[node].type === "folder") {
            return (
                <Button className="treeFoldButton"
                        disabled={this.state.fileStructure[node].children === undefined || this.state.fileStructure[node].children == null || this.state.fileStructure[node].children[0] === undefined}
                        onClick={() => {
                            let newStructure = this.state.fileStructure;
                            newStructure[node].open = !newStructure[node].open;
                            this.setState({
                                fileStructure: newStructure
                            })
                        }}
                        ghost
                >
                    <Icon type={this.state.fileStructure[node].open ? "caret-down" : "caret-right"}
                          style={{
                              marginRight: 4,
                              color: (this.state.fileStructure[node].children !== undefined && this.state.fileStructure[node].children != null && this.state.fileStructure[node].children[0] !== undefined) ? "#ADADAD" : "#3C3F41"
                          }}
                    />
                </Button>
            )
        }
    }

    renderWrite(node) {
        if (this.state.fileStructure[node].type !== "folder" && this.state.fileStructure[node].type !== "class") {
            return (
                <Button className="editButton"
                        ghost
                        onClick={() => {
                            let codeForm = {
                                name: this.state.fileName,
                                path: this.state.filePath,
                                language: this.state.language,
                                code: this.state.code
                            };

                            fetch(serverIp + "/save",
                                {
                                    method: "post",
                                    headers: {
                                        "Content-Type": "application/json",
                                        "Accept": "application/json"
                                    },
                                    body: JSON.stringify(codeForm),
                                    credentials: 'include'
                                }
                            );
                            let i = node;
                            let path = "";
                            while (this.state.fileStructure[i].parent !== -1) {
                                path = this.state.fileStructure[i].name + "/" + path;
                                i = this.state.fileStructure[i].parent;
                            }


                            this.setState({
                                fileName: this.state.fileStructure[node].name,
                                filePath: path.substr(0, path.substr(0, path.length - 1).lastIndexOf("/"))
                            }, () => {
                                path = path.substr(0, path.length - 1);
                                path += "." + this.state.fileStructure[node].type;
                                this.openFile(path);
                            });
                        }}
                >
                    <Icon type="edit"
                          className="editButton"
                          style={{
                              marginLeft: 4,
                              color: "#ADADAD"
                          }}
                    />
                </Button>
            )
        }
    }

    renderIcon(n) {
        if (n === "folder") {
            return (
                <Icon
                    type="folder"
                    theme="filled"
                    style={{
                        color: "#87939A",
                        marginRight: 4
                    }}
                />
            )
        } else if (n === "java") {
            return (
                <Icon
                    type="code"
                    theme="filled"
                    style={{
                        marginLeft: 18,
                        marginRight: 5,
                        marginTop: 4,
                        marginBottom: 6
                    }}
                />
            )
        } else {
            return (<Icon
                    type="file"
                    theme="filled"
                    style={{
                        color: "#87939A",
                        marginLeft: 18,
                        marginRight: 5,
                        marginTop: 4,
                        marginBottom: 6
                    }}
                />
            )
        }
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

    toggleRunning() {
        this.setState({
            isRunning: !this.state.isRunning
        });
    }

    sendMessage = (instance, changeObj) => {
        if (changeObj.text[0] === " " || changeObj.text[0] === "\n" || changeObj.text[0] === "\t") {
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
        } else if (changeObj.text[0] === "\b") {
            this.msgBuffer = this.msgBuffer.substring(0, this.msgBuffer.length - 1);
        } else {
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
    };

    kill() {
        fetch(serverIp + "/stop",
            {
                method: "post",
                credentials: 'include'
            }
        );
    }

    createFile() {
        let order = {
            name: this.state.newName,
            path: this.state.newPath,
            type: this.state.newFileType
        };

        fetch(serverIp + "/new-file",
            {
                method: "post",
                credentials: 'include',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(order),
            }
        ).then(this.updateFileTree);
    }

    handleFileNewClick(key) {
    }

    openModal = type => {
        this.setState({
            creatingFile: true,
            newFileType: type
        });
    };

    handleMenuClick(modalFunction, item) {
        const key = item.key;
        if (key === "newFolder") {
            modalFunction("folder");
        } else if (key === "newJava") {
            modalFunction("java source file");
        } else if (key === "newText") {
            modalFunction("text file");
        } else if (key === "newOther") {
            modalFunction("file");
        }
    }

    handleNewNameChange(event) {
        this.setState({newName: event.target.value});
    }

    handleNewPathChange(event) {
        this.setState({newPath: event.target.value});
    }

    openFile(path) {
        fetch(serverIp + "/load",
            {
                method: "post",
                credentials: 'include',
                headers: {
                    "Accept": "application/json"
                },
                body: path
            }
        ).then(response => response.json())
            .then(data => {
                this.setState({
                    code: data[0]
                })
            })

    };
}

/*
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
    	System.out.print("index?: ");
        int n = in.nextInt();
        System.out.println(f(n));
    }

    static int f(int n) {
        if(n <= 1) return 1;
        return f(n-1) + f(n-2);
    }
}
 */