import React from 'react';
import { shallow } from 'enzyme';
import { WMRaisedButton } from '@workmarket/front-end-components';
import DocumentRequirement from '../DocumentRequirement';
import { initialState } from '../../../reducers';

describe('<DocumentRequirement />', () => {
	const dummyFunc = jest.fn();
	const requirementComponentData = initialState.get('requirementsData').toJS();
	describe('Rendering consistency', () => {
		it('should render the same way every time with all props', () => {
			const snapshot = shallow(
				<DocumentRequirement.WrappedComponent
					handleChange={ dummyFunc }
					applyRequirement={ dummyFunc }
					requirementComponentData={ requirementComponentData }
				/>,
			);
			expect(snapshot).toMatchSnapshot();
		});

		describe('Interaction', () => {
			let wrapper;
			let button;

			const shallowRenderComponent = () => shallow(
				<DocumentRequirement.WrappedComponent
					handleChange={ dummyFunc }
					applyRequirement={ dummyFunc }
					requirementComponentData={ requirementComponentData }
				/>
			);

			beforeEach(() => {
				wrapper = shallowRenderComponent();
				button = wrapper.find(WMRaisedButton);
			});

			it('should have a <WMRaisedButton /> add button set to disabled', () => {
				const addButton = button.get(0);
				expect(addButton.props.disabled).toBeTruthy();
			});
		});
	});
});
