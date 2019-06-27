import {Link} from 'react-router-dom'
import "./Teacher.css"
import {Layout, Menu, Table} from "antd";

const React = require('react');
const {Header, Content} = Layout;
const SubMenu = Menu.SubMenu

const dataSource = [
    {
        key: '1',
        name: 'Mike',
        grade: 9,
        time: '0:23',
    },
    {
        key: '2',
        name: 'John',
        grade: 11,
        time: '1:34',
    },
];

const columns = [
    {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: 'Grade',
        dataIndex: 'grade',
        key: 'grade',
    },
    {
        title: 'Time Spent Working',
        dataIndex: 'time',
        key: 'time',
    },
];


export default class Teacher extends React.Component {
    render() {
        return (
            <Layout style={{overflow: "hidden"}}>
                <Header style={{height: '5vh'}}>
                    <Menu
                        theme="dark"
                        mode="horizontal"
                        style={{lineHeight: '5vh'}}
                    >
                        <SubMenu title="Classes" key="left" style={{float: 'left'}}>
                            <Menu.Item key="1">Class A</Menu.Item>
                            <Menu.Item key="2">Class B</Menu.Item>

                        </SubMenu>
                        <SubMenu title="Welcome Name profileicon" key="right" style={{float: 'right'}}>
                            <Menu.Item key="Logout">Logout</Menu.Item>

                        </SubMenu>
                    </Menu>
                </Header>
                <Content>
                    <Table dataSource={dataSource} columns={columns} />;
                </Content>
            </Layout>
        )
    }
}