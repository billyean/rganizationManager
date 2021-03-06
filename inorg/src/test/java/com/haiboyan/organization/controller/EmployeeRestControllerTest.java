/*
 * Copyright [2017] [Haibo(Tristan) Yan]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haiboyan.organization.controller;


import com.haiboyan.organization.OrganizationApplication;
import com.haiboyan.organization.model.Action;
import com.haiboyan.organization.model.Employee;
import com.haiboyan.organization.model.Role;
import com.haiboyan.organization.model.Team;
import com.haiboyan.organization.repository.EmployeeRepository;
import com.haiboyan.organization.repository.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrganizationApplication.class)
@WebAppConfiguration
public class EmployeeRestControllerTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private Team company;

    private Employee ceo;

    private Team l1Team1, l1Team2;

    private Employee vp1, vp2;

    private Team l2Team1_1, l2Team1_2, l2Team1_3, l2Team1_4, l2Team2_1, l2Team2_2, l2Team2_3;

    private Employee director1_1, director1_2, director1_3, director1_4, director2_1, director2_2, director2_3;

    private Team l3Team1_1_1, l3Team1_1_2, l3Team1_2_1, l3Team1_2_2, l3Team1_2_3, l3Team1_2_4, l3Team1_3_1, l3Team1_4_1,
            l3Team2_2_1, l3Team2_2_2, l3Team2_2_3, l3Team2_2_4, l3Team2_3_1;

    private Employee manager1_1_1, manager1_1_2, manager1_2_1, manager1_2_2, manager1_2_3, manager1_2_4, manager1_3_1, manager1_4_1,
            manager2_2_1, manager2_2_2, manager2_2_3, manager2_2_4, manager2_3_1;

    private List<Employee> employees1_1_1;

    private List<Employee> employees1_2;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        company = new Team("Test corp");

        l1Team1 = new Team("l1Team1");
        l1Team2 = new Team("l1Team2");

        l2Team1_1 = new Team("l2Team1_1");
        l2Team1_2 = new Team("l2Team1_2");
        l2Team1_3 = new Team("l2Team1_3");
        l2Team1_4 = new Team("l2Team1_4");
        l2Team2_1 = new Team("l2Team2_1");
        l2Team2_2 = new Team("l2Team2_2");
        l2Team2_3 = new Team("l2Team2_3");

        l3Team1_1_1 = new Team("l3Team1_1_1");
        l3Team1_1_2 = new Team("l3Team1_1_2");
        l3Team1_2_1 = new Team("l3Team1_2_1");
        l3Team1_2_2 = new Team("l3Team1_2_2");
        l3Team1_2_3 = new Team("l3Team1_2_3");
        l3Team1_2_4 = new Team("l3Team1_2_4");
        l3Team1_3_1 = new Team("l3Team1_3_1");
        l3Team1_4_1 = new Team("l3Team1_4_1");
        l3Team2_2_1 = new Team("l3Team2_2_1");
        l3Team2_2_2 = new Team("l3Team2_2_2");
        l3Team2_2_3 = new Team("l3Team2_2_3");
        l3Team2_2_4 = new Team("l3Team2_2_4");
        l3Team2_3_1 = new Team("l3Team2_3_1");


        int dayBack = 1000;

        // One CEO Created
        ceo = new Employee("CEO1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.CEO);
        employeeRepository.save(ceo);

        // In company level, CEO is the supervisor.
        company.setManager(ceo);
        teamRepository.save(company);

        // Set VP level team and employee
        vp1 = new Employee("VP1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.VP);
        vp1.setTeam(company);
        employeeRepository.save(vp1);
        l1Team1.setManager(vp1);
        teamRepository.save(l1Team1);

        vp2 = new Employee("VP2", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.VP);
        vp2.setTeam(company);
        employeeRepository.save(vp2);
        l1Team2.setManager(vp2);
        teamRepository.save(l1Team2);

        // Set Director level team and employee
        director1_1 = new Employee("Direct1_1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Director);
        director1_1.setTeam(l1Team1);
        employeeRepository.save(director1_1);
        l2Team1_1.setManager(director1_1);
        teamRepository.save(l2Team1_1);

        director1_2 = new Employee("Direct1_2", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Director);
        director1_2.setTeam(l1Team1);
        employeeRepository.save(director1_2);
        l2Team1_2.setManager(director1_2);
        teamRepository.save(l2Team1_2);

        director1_3 = new Employee("Direct1_3", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Director);
        director1_3.setTeam(l1Team1);
        employeeRepository.save(director1_3);
        l2Team1_3.setManager(director1_3);
        teamRepository.save(l2Team1_3);

        director1_4 = new Employee("Direct1_4", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Director);
        director1_4.setTeam(l1Team1);
        employeeRepository.save(director1_4);
        l2Team1_4.setManager(director1_4);
        teamRepository.save(l2Team1_4);

        director2_1 = new Employee("Direct2_1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Director);
        director1_1.setTeam(l1Team2);
        employeeRepository.save(director2_1);
        l2Team2_1.setManager(director2_1);
        teamRepository.save(l2Team2_1);

        director2_2 = new Employee("Direct2_2", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Director);
        director2_2.setTeam(l1Team2);
        employeeRepository.save(director2_2);
        l2Team2_2.setManager(director2_2);
        teamRepository.save(l2Team2_2);

        director2_3 = new Employee("Direct2_3", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Director);
        director2_3.setTeam(l1Team2);
        employeeRepository.save(director2_3);
        l2Team2_3.setManager(director2_3);
        teamRepository.save(l2Team2_3);

        // Set Manager level team and employee
        manager1_1_1 = new Employee("Manager1_1_1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager1_1_1.setTeam(l2Team1_1);
        manager1_1_1 = employeeRepository.save(manager1_1_1);
        l3Team1_1_1.setManager(manager1_1_1);
        teamRepository.save(l3Team1_1_1);

        manager1_1_2 = new Employee("Manager1_1_2", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager1_1_2.setTeam(l2Team1_1);
        employeeRepository.save(manager1_1_2);
        l3Team1_1_2.setManager(manager1_1_2);
        teamRepository.save(l3Team1_1_2);

        manager1_2_1 = new Employee("Manager1_2_1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager1_2_1.setTeam(l2Team1_2);
        employeeRepository.save(manager1_2_1);
        l3Team1_2_1.setManager(manager1_2_1);
        teamRepository.save(l3Team1_2_1);

        manager1_2_2 = new Employee("Manager1_2_2", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager1_2_2.setTeam(l2Team1_2);
        employeeRepository.save(manager1_2_2);
        l3Team1_2_2.setManager(manager1_2_2);
        teamRepository.save(l3Team1_2_2);

        manager1_2_3 = new Employee("Manager1_2_3", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager1_2_3.setTeam(l2Team1_2);
        employeeRepository.save(manager1_2_3);
        l3Team1_2_3.setManager(manager1_2_3);
        teamRepository.save(l3Team1_2_3);

        manager1_2_4 = new Employee("Manager1_2_4", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager1_2_4.setTeam(l2Team1_2);
        employeeRepository.save(manager1_2_4);
        l3Team1_2_4.setManager(manager1_2_4);
        teamRepository.save(l3Team1_2_4);

        manager1_3_1 = new Employee("Manager1_3_1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager1_3_1.setTeam(l2Team1_3);
        employeeRepository.save(manager1_3_1);
        l3Team1_3_1.setManager(manager1_3_1);
        teamRepository.save(l3Team1_3_1);

        manager1_4_1 = new Employee("Manager1_4_1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager1_4_1.setTeam(l2Team1_4);
        employeeRepository.save(manager1_4_1);
        l3Team1_4_1.setManager(manager1_4_1);
        teamRepository.save(l3Team1_4_1);

        manager2_2_1 = new Employee("Manager2_2_1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager2_2_1.setTeam(l2Team2_2);
        employeeRepository.save(manager2_2_1);
        l3Team2_2_1.setManager(manager2_2_1);
        teamRepository.save(l3Team2_2_1);

        manager2_2_2 = new Employee("Manager2_2_2", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager2_2_2.setTeam(l2Team2_2);
        employeeRepository.save(manager2_2_2);
        l3Team2_2_2.setManager(manager2_2_2);
        teamRepository.save(l3Team2_2_2);

        manager2_2_3 = new Employee("Manager2_2_3", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager2_2_3.setTeam(l2Team2_2);
        employeeRepository.save(manager2_2_3);
        l3Team2_2_3.setManager(manager2_2_3);
        teamRepository.save(l3Team2_2_3);

        manager2_2_4 = new Employee("Manager2_2_4", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager2_2_4.setTeam(l2Team2_2);
        employeeRepository.save(manager2_2_4);
        l3Team2_2_4.setManager(manager2_2_4);
        teamRepository.save(l3Team2_2_4);

        manager2_3_1 = new Employee("Manager2_3_1", LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Manager);
        manager2_3_1.setTeam(l2Team2_3);
        employeeRepository.save(manager2_3_1);
        l3Team2_3_1.setManager(manager2_3_1);
        teamRepository.save(l3Team2_3_1);

        // Create 10 employee for l3Team1_1_1
        employees1_1_1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee("l3Team1_1_1 Employee_" + i, LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Permanent_Employee);
            employee.setTeam(l3Team1_1_1);
            employeeRepository.save(employee);
            employees1_1_1.add(employee);
        }

        // Create 20 employee for l2Team1_2
        employees1_2 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Employee employee = new Employee("l2Team1_2 Employee_" + i, LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Permanent_Employee);
            employee.setTeam(l2Team1_2);
            employeeRepository.save(employee);
            employees1_2.add(employee);
        }

        // Create 20 employee for l3Team1_2_1
        for (int i = 0; i < 20; i++) {
            Employee employee = new Employee("l3Team1_2_1 Employee_" + i, LocalDate.now().minus(Period.ofDays(5 * dayBack--)), Role.Permanent_Employee);
            employee.setTeam(l3Team1_2_1);
            employeeRepository.save(employee);
        }
    }

    @Test
    public void testAddEmployee() throws Exception {
        int index = 1;
        for (Role role : EnumSet.allOf(Role.class)) {
            Employee employee = new Employee("User" + index, LocalDate.now(), role);
            if (role == Role.CEO) {
                this.mockMvc.perform(post("/employee")
                        .content(json(employee))
                        .contentType(contentType))
                        .andExpect(status().is4xxClientError());
            } else {
                this.mockMvc.perform(post("/employee")
                        .content(json(employee))
                        .contentType(contentType))
                        .andExpect(status().isCreated());
            }
        }
    }

    @Test
    public void testOnLeave() throws Exception {
        // Test manager1_1_1 on leave

        //Before manager1_1_1 on leave, director1_1 has 2 direct report manager1_1_1, manager1_1_2
        this.mockMvc.perform(get("/employee/" + director1_1.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        // manager1_1_1 is on leave
        this.mockMvc.perform(post("/employee/" + manager1_1_1.getId() + "/action")
                .content(json(new Action("onLeave")))
                .contentType(contentType))
                .andExpect(status().isOk());

        // After manager1_1_1 on leave, all people reported to manager1_1_1 will be reporting to director1_1, which makes
        // director1_1 has 11 direct report
        this.mockMvc.perform(get("/employee/" + director1_1.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(11)));

        // manager1_1_1 finish on leave
        this.mockMvc.perform(post("/employee/" + manager1_1_1.getId() + "/action")
                .content(json(new Action("comBack")))
                .contentType(contentType))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/employee/" + director1_1.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        this.mockMvc.perform(get("/employee/" + manager1_1_1.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));

    }

    /**
     * An employee can move to another team  within the organisation. When moving to a different team, an employee
     * starts reporting to a new manager, without transferring his past subordinates to the new team. Instead, the most
     * senior (based on start date) of his subordinates should be promoted to manage the employee’s former team.
     *
     * @throws Exception
     */
    @Test
    public void testOnMove() throws Exception {
        // direct1_2 has 24 direct report, one manager and 20 permanent employee.
        this.mockMvc.perform(get("/employee/" + director1_2.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(24)));

        // vp1 is direct1_2's manager, has 4 direct report, which is direct1_1, direct1_2, direct1_3, direct1_4
        this.mockMvc.perform(get("/employee/" + vp1.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        // direct1_2 make a move
        this.mockMvc.perform(post("/employee/" + director1_2.getId() + "/team/" + l1Team2.getId())
                .contentType(contentType))
                .andExpect(status().isOk());

        // After move direct1_2 will have no direct report.
        this.mockMvc.perform(get("/employee/" + director1_2.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // vp1 will still have 4 direct reports direct1_1, direct1_3, direct1_4, and most senior one in l2Team1_2
        this.mockMvc.perform(get("/employee/" + vp1.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        Employee mostSenior = manager1_2_1;
        this.mockMvc.perform(get("/employee/" + mostSenior.getId() + "/direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(24)));
    }

    @Test
    public void testOnPromoteFailure1() throws Exception {
        // director1_1 only has 12 total reports, should not be able to be promoted
        this.mockMvc.perform(post("/employee/" + director1_1.getId() + "/action")
                .content(json(new Action("promote")))
                .contentType(contentType))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testOnPromoteSuccess() throws Exception {
        this.mockMvc.perform(get("/employee/" + director1_2.getId() + "/manager")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("VP1")));

        // director1_1 only has 12 total reports, should not be able to be promoted
        this.mockMvc.perform(post("/employee/" + director1_2.getId() + "/action")
                .content(json(new Action("promote")))
                .contentType(contentType))
                .andExpect(status().isOk());


        this.mockMvc.perform(get("/employee/" + director1_2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("VP")));

        this.mockMvc.perform(get("/employee/" + director1_2.getId() + "/manager")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("CEO1")));
    }


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
