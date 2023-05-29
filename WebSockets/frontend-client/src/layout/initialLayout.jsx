import React from "react";
import {Container, Navbar} from "react-bootstrap";

function InitialLayout(props) {
    return (
        <>
        <Navbar bg="dark" variant="dark">
            <Container>
                <Navbar.Brand className={'navbar-brand mx-auto'}>
                    Chat Demo
                </Navbar.Brand>
            </Container>
        </Navbar>
            {props.children}
        </>

    )
}

export default InitialLayout;
