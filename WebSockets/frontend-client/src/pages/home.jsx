import React, {useEffect, useState} from "react";
import {Button, Col, Form, Row, Table} from "react-bootstrap";
import AuthService from "../services/AuthService.js";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function Home() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [users, setUsers] = useState([]);
    const auth = AuthService

    useEffect(() => {
        initSocket();
    }, []);

    function initSocket() {
        let connection = new WebSocket("ws://localhost:8080/Chat-war/ws/chat"); // 'chat' should be individual person's username; ovaj chat je kao username
        //trebalo bi otvoriti sesiju prilikom logovanja sa usernameom tog ulogovanog korisnika da se zna kom korisniku treba poslati poruku
        connection.onopen = function () {
            console.log("Socket is open");
        }

        connection.onclose = function () {
            connection = null;
        }

        connection.onmessage = function (msg) {
            console.log("ON MESIDZ!!!", msg);
            const data = msg.data.split("!");
            if(data[0] === "LOGGEDIN") {
                let users = [];
                data[1].split("|").forEach(user => {
                    if (user) {
                        let userData = user.split(",");
                        users.push({
                            username: userData[0],
                            password: userData[1]
                        })
                    }
                });
                setUsers(users);
            } else {
                toast(data[1]);
            }
        }

    }

    function register() {
        AuthService.register({
            username: username,
            password: password
        });
    }

    function login() {
        AuthService.login({
            username: username,
            password: password
        });
    }

    function getUsers() {
        AuthService.getUsers();
    }

    const userRows = users.map((user, key) => (
        <tr>
            <td>{user.username}</td>
            <td>{user.password}</td>
        </tr>
    ))

    return (
        <div>
            <Row><Col md={{span: 4, offset: 4}}>
                <Row className={'mt-5 mb-2'}>
                    <Form.Control placeholder="Enter username"
                                  value={username} onChange={e => setUsername(e.target.value)}/>
                </Row>
                <Row className={'mb-3'}>
                    <Form.Control type="password" placeholder="Enter password"
                                  value={password} onChange={e => setPassword(e.target.value)}/>
                </Row>
                <Row className={'mb-2'}>
                    <Button variant="primary" onClick={register}>Sign Up</Button>
                </Row>
                <Row className={'mb-2'}>
                    <Button variant="primary" onClick={login}>Log In</Button>
                </Row>
                <Row className={'mb-2'}>
                    <Button variant="outline-secondary" onClick={getUsers}>List all logged-in users</Button>
                </Row>
                <Row>
                    <Table striped bordered hover>
                        <thead>
                        <tr>
                            <th>Username</th>
                            <th>Password</th>
                        </tr>
                        </thead>
                        <tbody>
                            {userRows}
                        </tbody>
                    </Table>
                </Row>
            </Col></Row>
            <ToastContainer/>
        </div>
    );
}

export default Home;
